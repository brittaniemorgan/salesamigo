from flask import Flask, flash, redirect, url_for
from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager
from flask_mail import Mail
from dotenv import load_dotenv
import os
import pymysql

pymysql.install_as_MySQLdb()
load_dotenv()

db = SQLAlchemy()
login_manager = LoginManager()


def get_db_connection():
    import pymysql.cursors
    connection = pymysql.connect(
        host=os.getenv('MYSQL_DATABASE_HOST', 'localhost'),
        user=os.getenv('MYSQL_DATABASE_USER', 'swen3920'),
        password=os.getenv('MYSQL_DATABASE_PASSWORD', 'password123'),
        database=os.getenv('MYSQL_DATABASE_DB', 'sales_amigo'),
        charset=os.getenv('MYSQL_DATABASE_CHARSET', 'utf8'),
        cursorclass=pymysql.cursors.DictCursor
    )
    return connection

def create_app():
    app = Flask(__name__)
    app.config.from_object('app_ecommerce.config.Config')

    db.init_app(app)
    login_manager.init_app(app)
    login_manager.login_view = 'login'
    mail = Mail(app)

    @login_manager.unauthorized_handler
    def unauthorized():
        flash('Please log in to access this page.', 'danger')
        return redirect(url_for('login'))

    with app.app_context():
        from . import views, models
        db.create_all()

    return app

@login_manager.user_loader
def load_user(customer_id):
    from app_ecommerce.models import Customers
    return Customers.query.get(int(customer_id))