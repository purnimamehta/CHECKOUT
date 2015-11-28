from flask import Flask
app = Flask(__name__)

@app.route('/AnsafAhmad')
def hello_world():
    return 'True'

@app.route('/Fiza')
def Fiza():
    return 'fiza'

if __name__ == '__main__':
    app.run()