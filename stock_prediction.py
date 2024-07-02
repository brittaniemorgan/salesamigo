import pandas as pd
import numpy as np
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.metrics import mean_squared_error

import pandas as pd
import pandas as pd
import numpy as np
from statsmodels.tsa.stattools import adfuller
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import TimeSeriesSplit, cross_val_score

import pandas as pd
import numpy as np
from sklearn.ensemble import GradientBoostingRegressor, RandomForestRegressor
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import cross_val_score
from sklearn.metrics import mean_squared_error
from statsmodels.tsa.stattools import adfuller
from math import ceil

import mysql.connector
from datetime import datetime


app = Flask(__name__)
app.config['SECRET_KEY'] = 'your_secret_key_here'  # Set your secret key here
app.config['UPLOAD_FOLDER'] = 'images'  # Set the path to your uploaded images directory
csrf = CSRFProtect(app)

app.config['WTF_CSRF_ENABLED'] = False
def get_db_connection():
    connection = mysql.connector.connect(
    host = 'localhost',
    user= 'swen3920',
    password = 'password123',
    database = 'sales_amigo')
    return connection

def fetchSalesData():
    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)

    try:
        query = """SELECT * FROM sales_transactions t
                    JOIN transaction_items ti ON ti.transaction_id = r.transaction_id
                """
        cursor.execute(query)
        transaction_item = cursor.fetchall()
        data = [{
            'transaction_id': transaction_item['transaction_id'],
            'transaction_date': transaction_item['transaction_date'],
            'employee_id': transaction_item['employee_id'], 
            'customer_id': transaction_item['customer_id'],
            'total' : transaction_item['total'],
            'payment_method' : transaction_item['payment_method'],
            'transaction_item_id': transaction_item['transaction_item_id'], 
            'product_id': transaction_item['product_id'],       
            'quantity' : transaction_item['quantity'],
            'price' : transaction_item['price'],
            'year_month': transaction_item['year_month']
        } for transaction_item in transaction_items]
        df = pd.DataFrame(data)
        df['transaction_date'] = pd.to_datetime(df['transaction_date'])

        # Extract month and year from t_dat
        df['year_month'] = df['transaction_date'].dt.to_period('M')

        # Aggregate monthly sales for each article
        monthly_sales_df = df.groupby(['year_month', 'product_id']).size().reset_index(name='items_sold')
        return monthly_sales_df
        cursor.close()
        connection.close()
    except Exception as e:
        return str(e)

monthly_sales_df = fetchSalesData()

def adf_test(series):
    result = adfuller(series, autolag='AIC')
    print('ADF Statistic:', result[0])
    print('p-value:', result[1])
    for key, value in result[4].items():
        print('Critical Values:')
        print(f'   {key}, {value}')

# Apply differencing to remove trend and seasonality
monthly_sales_df['diff_items_sold'] = monthly_sales_df.groupby('product_id')['items_sold'].diff().dropna()

# Perform the ADF test on the differenced series
adf_test(monthly_sales_df['diff_items_sold'].dropna())
# Assuming you have monthly sales data
monthly_sales_featured = monthly_sales_df.copy()

# Extract month and year from the date column
monthly_sales_featured['month'] = monthly_sales_featured['year_month'].dt.month
monthly_sales_featured['year'] = monthly_sales_featured['year_month'].dt.year

# Sort values by date for correct calculation
monthly_sales_featured = monthly_sales_featured.sort_values(by=['product_id', 'year_month'])

# Aggregate total items sold per month for each product
monthly_sales_featured = monthly_sales_featured.groupby(['product_id', 'month', 'year'])['items_sold'].sum().reset_index()

# Feature Engineering
monthly_sales_featured['sin_month'] = np.sin(2 * np.pi * monthly_sales_featured['month'] / 12)
monthly_sales_featured['cos_month'] = np.cos(2 * np.pi * monthly_sales_featured['month'] / 12)

# Adding lagged features
for i in range(1, 2):
    monthly_sales_featured[f'lag_{i}'] = monthly_sales_featured.groupby('product_id')['items_sold'].shift(i)

# Assuming you have monthly sales data
monthly_sales_featured = monthly_sales_df.copy()
# Drop rows with NaN values (lags up to 6 months back)
monthly_sales_featured .dropna(inplace=True)

# Extract month and year from the date column
monthly_sales_featured['month'] = monthly_sales_featured ['year_month'].dt.month
monthly_sales_featured['year'] = monthly_sales_featured ['year_month'].dt.year

# Feature Engineering
monthly_sales_featured['sin_month'] = np.sin(2 * np.pi * monthly_sales_featured['month'] / 12)
monthly_sales_featured['cos_month'] = np.cos(2 * np.pi * monthly_sales_featured['month'] / 12)

# Adding lagged features (using fewer lags)
for i in range(1, 4):
    monthly_sales_featured[f'lag_{i}'] = monthly_sales_featured.groupby('product_id')['items_sold'].shift(i)

# Drop rows with NaN values (lags up to 6 months back)
monthly_sales_featured.dropna(inplace=True)



# Assuming you have monthly sales data
# Define features and target
X = monthly_sales_featured.drop(['product_id', 'items_sold', 'year_month'], axis=1)
y = monthly_sales_featured['items_sold']

# Models to test
models = {
    'Linear Regression': LinearRegression(),
    'Random Forest': RandomForestRegressor(n_estimators=100, random_state=0),
    'Gradient Boosting': GradientBoostingRegressor(n_estimators=100, learning_rate=0.1, max_depth=3, random_state=0)
}

# Cross-validation to find the best model
best_model = None
best_rmse = float('inf')

for name, model in models.items():
    scores = cross_val_score(model, X, y, scoring='neg_root_mean_squared_error', cv=tscv)
    avg_rmse = -scores.mean()
    model.fit(X, y)
    y_pred = model.predict(X)
    train_rmse = np.sqrt(mean_squared_error(y, y_pred))
    print(f"Model: {name}")
    print(f"Training RMSE: {train_rmse}")
    print(f"Cross-Validation RMSE: {avg_rmse}\n")
    if avg_rmse < best_rmse:
        best_rmse = avg_rmse
        best_model = model

print(f"Best Model: {best_model.__class__.__name__}")
print(f"Best Cross-Validation RMSE: {best_rmse}")

def predict_sales(model, product_id, month, year, lag_1, lag_2, lag_3, diff_items_sold):
    # Prepare the prediction data
    prediction_data = pd.DataFrame({
        'diff_items_sold': diff_items_sold,
        'month': [month],
        'year': [year],
        'sin_month': [np.sin(2 * np.pi * month / 12)],
        'cos_month': [np.cos(2 * np.pi * month / 12)],
        'lag_1': [lag_1],
        'lag_2': [lag_2],
        'lag_3': [lag_3],
    })

    # Make prediction
    forecast_quantity = model.predict(prediction_data)
    return ceil(forecast_quantity[0])

# Example usage:
# Assuming you have a trained model named `model` and the required prediction data
product_id = 7
month = 6
year = 2024
product_sales = monthly_sales_featured[monthly_sales_featured['product_id'] == product_id]
if len(product_sales) < 1:
    print("Insufficient data for prediction.")
else:
  lag_1 = product_sales['lag_1'].iloc[-1]
  lag_2 = product_sales['lag_2'].iloc[-1]
  lag_3 = product_sales['lag_3'].iloc[-1]
  diff_items_sold = product_sales['diff_items_sold'].iloc[-1]
  forecast_quantity = predict_sales(model, product_id, month, year, lag_1, lag_2, lag_3, diff_items_sold)
  print(f"Forecasted Quantity Sold: {forecast_quantity}")

  
if __name__ == '__main__':
    app.run(debug=True)
