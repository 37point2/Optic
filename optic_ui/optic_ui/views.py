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

@app.route('/tweets')
def tweets():
    query = "match Tweet return Tweet.text"
    data, metadata = cypher.execute(graphdb, query)
    data = filter(lambda x: x[0] is not None, data)
    return Response(json.dumps(data), content_type="application/json")

@app.route("/data")
def data():
    query = "start n=node:User('screen_name:37point2') match (n)-[r]-(x)-[s]-(m) return n,r,x,s,m limit 250"
    data, metadata = cypher.execute(graphdb, query)
    nodes = {}
    links = []
    ids = []
    for row in data:
        for item in row:
            if isinstance(item, neo4j.Node):
                if item._id not in ids:
                    ids.append(item._id)
                id = ids.index(item._id)
                type = [x for x in item.get_labels() if not x.startswith("_")][0]
                if type == "User":
                    name = item.get_properties()['screen_name']
                elif type == "Tweet":
                    name = item.get_properties()['text']
                elif type == "Tag":
                    name = item.get_properties()['tag']
                elif type == "Url":
                    name = item.get_properties()['url']
                nodes[name] = {"id":id,"type":type,"name":name}
    for row in data:
        for item in row:
            if isinstance(item, neo4j.Relationship):
                if item._id not in ids:
                    ids.append(item._id)
                if item.start_node._id not in ids:
                    ids.append(item.start_node._id)
                if item.end_node._id not in ids:
                    ids.append(item.end_node._id)
                links.append({"id":ids.index(item._id),"type":item.type,"source":ids.index(item.start_node._id),"target":ids.index(item.end_node._id)})

    res = {"nodes":[nodes[node] for node in sorted(nodes,key=lambda x:nodes[x]['id'])],"links":links}
    return Response(json.dumps(res), content_type="application/json")