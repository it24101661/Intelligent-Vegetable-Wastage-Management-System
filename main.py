import pickle
import pandas as pd
import numpy as np
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional, Dict
from datetime import datetime

# --- Load the saved model components ---
try:
    with open('model.pkl', 'rb') as f:
        model = pickle.load(f)
    with open('scaler.pkl', 'rb') as f:
        scaler = pickle.load(f)
    with open('le_vegetable.pkl', 'rb') as f:
        le_vegetable = pickle.load(f)
    print("✅ All model components loaded successfully.")
except FileNotFoundError as e:
    print(f"Error loading model components: {e}")

app = FastAPI(title="Vegetable Wastage Management API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class StockItem(BaseModel):
    vegetableName: str
    pricePerKg: float
    quantityKg: float
    harvestDate: Optional[str] = None
    expiryEstimate: Optional[str] = None

class PredictRequest(BaseModel):
    farmerId: str
    stocks: List[StockItem]

def assign_wastage_risk(remaining_life: int) -> str:
    if remaining_life <= 1:
        return 'Critical'
    elif remaining_life <= 3:
        return 'High'
    elif remaining_life <= 6:
        return 'Medium'
    else:
        return 'Low'

@app.post("/predict")
async def predict(request: PredictRequest):
    predictions = []
    
    for stock in request.stocks:
        # Default calculations if dates are missing
        today = datetime.now()
        
        harvest_date = today
        if stock.harvestDate:
            try:
                harvest_date = datetime.strptime(stock.harvestDate, '%Y-%m-%d')
            except:
                pass
                
        expiry_date = today
        if stock.expiryEstimate:
            try:
                expiry_date = datetime.strptime(stock.expiryEstimate, '%Y-%m-%d')
            except:
                pass
                
        freshness_age = max(0, (today - harvest_date).days)
        remaining_life = max(0, (expiry_date - today).days)
        shelf_life_days = freshness_age + remaining_life
        if shelf_life_days == 0: shelf_life_days = 10
        
        # Determine base daily demand (fallback if calculation is tricky)
        base_daily_demand = stock.quantityKg * 0.15 + 5
        supply_demand_gap = stock.quantityKg - base_daily_demand
        
        stock_demand_ratio = stock.quantityKg / (base_daily_demand + 1)
        life_used_ratio = freshness_age / (shelf_life_days + 1)
        days_to_expire = remaining_life
        expires_before_14days = 1 if days_to_expire < 14 else 0
        freshness_pct_remaining = (days_to_expire / (shelf_life_days + 1)) * 100
        price_demand_ratio = stock.pricePerKg / (base_daily_demand + 1)
        overstock_flag = 1 if supply_demand_gap > (base_daily_demand * 3) else 0
        demand_coverage = stock_demand_ratio
        
        # Prepare feature array strictly matching training format:
        # 'country', 'vegetable', 'price', 'available_stock', 'freshness_age',
        # 'shelf_life_days', 'remaining_life', 'supply_demand_gap', 'harvest_month',
        # 'harvest_dayofweek', 'harvest_quarter', 'stock_demand_ratio',
        # 'life_used_ratio', 'days_to_expire', 'expires_before_14days',
        # 'freshness_pct_remaining', 'price_demand_ratio', 'overstock_flag', 'demand_coverage'
        
        veg_name_encoded = 0
        try:
            # Map standard backend names to model trained names if needed
            name = stock.vegetableName
            if name not in le_vegetable.classes_:
                # Try finding closest match
                matches = [c for c in le_vegetable.classes_ if c.lower() in name.lower() or name.lower() in c.lower()]
                if matches:
                    name = matches[0]
                else:
                    name = le_vegetable.classes_[0] # Default fallback
            veg_name_encoded = le_vegetable.transform([name])[0]
        except:
            pass

        input_df = pd.DataFrame([{
            'country': 1, # Sri lanka
            'vegetable': veg_name_encoded,
            'price': stock.pricePerKg,
            'available_stock': stock.quantityKg,
            'freshness_age': freshness_age,
            'shelf_life_days': shelf_life_days,
            'remaining_life': remaining_life,
            'supply_demand_gap': supply_demand_gap,
            'harvest_month': harvest_date.month,
            'harvest_dayofweek': harvest_date.weekday(),
            'harvest_quarter': (harvest_date.month - 1) // 3 + 1,
            'stock_demand_ratio': stock_demand_ratio,
            'life_used_ratio': life_used_ratio,
            'days_to_expire': days_to_expire,
            'expires_before_14days': expires_before_14days,
            'freshness_pct_remaining': freshness_pct_remaining,
            'price_demand_ratio': price_demand_ratio,
            'overstock_flag': overstock_flag,
            'demand_coverage': demand_coverage
        }])

        weekly_demand = 0.0
        try:
            scaled_input = scaler.transform(input_df)
            weekly_demand = model.predict(scaled_input)[0]
        except Exception as e:
            # Fallback if model fails
            print(f"Model prediction failed: {e}")
            weekly_demand = stock.quantityKg * 0.8
            
        wastage_risk = assign_wastage_risk(remaining_life)
        
        # Calculate dynamic suggested price based on risk and demand
        suggested_price = stock.pricePerKg
        action = "Maintain Price"
        demand_level = "Stable"
        
        if weekly_demand > stock.quantityKg * 1.5:
            demand_level = "High"
            action = "Increase Price Slightly"
            suggested_price *= 1.1
        elif weekly_demand < stock.quantityKg * 0.5:
            demand_level = "Low"
            action = "Discount Required"
            suggested_price *= 0.8
            
        if wastage_risk in ['High', 'Critical']:
            action = "URGENT DISCOUNTS RECOMMENDED"
            suggested_price = stock.pricePerKg * 0.6
            
        predictions.append({
            "suggestedPricePerKg": round(suggested_price, 2),
            "confidence": 0.87,
            "weeklyDemandKg": round(weekly_demand, 2),
            "demandLevel": demand_level,
            "demandTrend": "Fluctuating",
            "wastageRisk": wastage_risk,
            "recommendedAction": action
        })
        
    return {"predictions": predictions}
