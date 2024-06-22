import os

class Config:
    SECRET_KEY = os.environ.get('SECRET_KEY', 'sOm3$ecRetK*y')
    SQLALCHEMY_DATABASE_URI = (
        os.environ.get('DATABASE_URL') or 
        'mysql+pymysql://swen3920:password123@localhost/sales_amigo'
    )
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    MAIL_SERVER = os.environ.get('MAIL_SERVER', 'localhost')
    MAIL_PORT = os.environ.get('MAIL_PORT', '25')
    MAIL_USERNAME = os.environ.get('MAIL_USERNAME')
    MAIL_PASSWORD = os.environ.get('MAIL_PASSWORD')