from flask import Flask, render_template, request, redirect, url_for, jsonify, send_from_directory, abort
from flask_wtf.csrf import CSRFProtect
from flask_wtf.csrf import generate_csrf
import os
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


@app.route('/')
def hello():
    data = {'message': 'Hello from API'}
    return jsonify(data)

@app.route('/register_user', methods=['POST'])
def register():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()
        content = request.json
        username = content['username']
        password = content['password']
        role = content['role']
        query = "INSERT INTO User (username, password, role) VALUES (%s, %s, %s)"
        cursor.execute(query, (username, password, role))
        connection.commit()

        return jsonify({"success" : "User added"})

    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/login', methods=['POST'])
def login():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)
        content = request.json
        employee_id = content['employee_id']
        password = content['password']
        cursor.execute('SELECT * FROM employees WHERE employee_id = %s', (employee_id,))
        user = cursor.fetchone()
        if user is None:
            return jsonify({"error": "Invalid Employee ID"}), 400
        
        cursor.execute('SELECT * FROM employees WHERE employee_id = %s AND password = %s', (employee_id, password))
        user = cursor.fetchone()

        if user is not None:
           # user.pop('password', None)
            return jsonify({"message": "User logged in successfully",
                            "user":user}), 200
        else:
            return jsonify({"error": "Invalid password"}), 400  

    except Exception as e:
        return jsonify({"error": str(e)})
    

@app.route('/employees', methods=['GET'])
def get_users():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)
        query = f"SELECT * FROM employees"
        cursor.execute(query)
        result = cursor.fetchall()

        users = []
        for user in result:
            users.append(user)
        cursor.close()
        connection.close()
        return jsonify(users=users) 

    except Exception as e:
        return jsonify({"error": str(e)})

@app.route('/csrf-token', methods=['GET']) 
def get_csrf(): 
    return jsonify({'csrf_token': generate_csrf()}) 

@app.route('/products', methods=['POST'])
def add_item():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        category = content['category']
        price = content['price']
        description = content['description']
        min_quantity = content['min_quantity']
        quantity = content['quantity']

        current_date = datetime.now().strftime('%Y-%m-%d %H:%M:%S')

        query = """INSERT INTO product_inventory (name, category, price, description, quantity, minimum_quantity, date_added, last_date_updated) 
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"""
        cursor.execute(query, (name, category, price, description, quantity, min_quantity, current_date, current_date))
        connection.commit()
        cursor.close()
        connection.close()
        return jsonify({"success": "Product added"})
    
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/products', methods=['PUT'])
def update_product():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        product_id = content['product_id']
        name = content['name']
        category = content['category']
        price = content['price']
        description = content['description']
        min_quantity = content['min_quantity']
        quantity = content['quantity']
        query = "SELECT * FROM product_inventory WHERE product_id = %s"
        cursor.execute(query, (product_id,))
        product = cursor.fetchone()

        if not product:
            return jsonify({"error": f"Product with ID {product_id} not found"}), 404

        query = """UPDATE product_inventory SET 
                          name = %s,
                          category = %s,
                          price = %s,
                          description = %s,
                          minimum_quantity = %s,
                          quantity = %s
                          WHERE product_id = %s"""

        cursor.execute(query, (name, category, price, description, min_quantity, quantity, product_id))
        connection.commit()

        return jsonify({"success": "Product updated successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/products', methods=['GET'])
def view_products():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM product_inventory"
        cursor.execute(query)
        products = cursor.fetchall()

        return jsonify({"products": products}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True)
