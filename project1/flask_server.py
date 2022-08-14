import os
from flask import Flask, request
from werkzeug.utils import secure_filename

UPLOAD_FOLDER = 'D:/mp4'


app = Flask(__name__)

@app.route('/', methods = ['GET', 'POST'])
def hello_world():

    return "Hello World"

@app.route('/post', methods = ['GET', 'POST'])
def hello_post():
    value = request.form['value']
    print(value)
    return (value)

@app.route('/mp4', methods=['POST'])
def upload_video():

    file = request.files['file']
    app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
    
    #filename = secure_filename(file.filename)
    filename = request.form['filename']
    print(filename)

    file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
    print('upload_video filename: ' + filename)
    return ('The video has been successfully uploaded to Flask Server')
    
if __name__=="__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
