document.getElementById('orderForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const order = {
        userId: document.getElementById('userId').value,
        vegId: document.getElementById('vegId').value,
        qty: document.getElementById('qty').value,
        deliveryDate: document.getElementById('deliveryDate').value
    };

    fetch('/api/orders', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(order)
    })
    .then(res => res.json())
    .then(() => {
        alert('Order Saved!');
        loadOrders();
    });
});

function loadOrders() {
    fetch('/api/orders')
    .then(res => res.json())
    .then(data => {
        const list = document.getElementById('orders');
        list.innerHTML = '';
        data.forEach(o => {
            const li = document.createElement('li');
            li.textContent = `ID: ${o.id} | User: ${o.userId} | Veg: ${o.vegId} | Qty: ${o.qty} | Date: ${o.deliveryDate}`;
            list.appendChild(li);
        });
    });
}
