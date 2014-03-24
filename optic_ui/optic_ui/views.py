from flask import render_template
from flask import request, Response
from optic_ui import app

import json
import numpy as np
from py2neo import neo4j,cypher

graphdb = neo4j.GraphDatabaseService("http://127.0.0.1:7474/db/data/")

title="Optic"

@app.route('/')
@app.route('/index')
def index():
    return render_template("index.html", title=title)

@app.route('/login', methods=['GET','POST'])
def login():
    if request.method == 'POST':
        #fix later, no auth implemented yet
        return render_template('index.html', title=title)
    else:
        return render_template("login.html", title=title)

@app.route('/force')
def force():
    return render_template('force.html', title=title)

@app.route("/data")
def data():
    query = "MATCH (a)-[r]-(b) RETURN a,b LIMIT 100"
    data, metadata = cypher.execute(graphdb, query)
    res = []
    for row in data:
        res.append([row[0].get_properties(),row[1].get_properties()])
    return Response(json.dumps(res), content_type="application/json")