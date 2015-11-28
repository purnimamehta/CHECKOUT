from flask import Flask, render_template,g,request
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
def store():
	 username = request.args.get('username')
	 password = request.args.get('password')
	 return str(name) + " " + str(password)




if __name__ == '__main__':
    app.run()