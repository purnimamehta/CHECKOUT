from flask import Flask,request
from porc import Client
API_KEY = '16509990-f7fc-48dc-b128-716c99b65a24'
client = Client(API_KEY)



app = Flask(__name__)

@app.route('/store', methods = ['GET', 'POST'])
def store():
	 store = request.args.get('store')
	 return str(store)

@app.route('/upc', methods = ['GET', 'POST'])
def upc():
	 name = request.args.get('upc')
	 return str(name)

@app.route('/user', methods = ['GET', 'POST'])
def user():
	 username = request.args.get('username')
	 password = request.args.get('password')
	 
	 result = False

	 users_check = client.get('users',username)

	 if(users_check['email'] == username and users_check['password'] == password):
	 	result = True

	 return str(result)




if __name__ == '__main__':
    app.run(host = '0.0.0.0')