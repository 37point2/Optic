package com.rlilly.optic.ingest.es;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.node.Node;

public class Client {

	private Node _node;
	private org.elasticsearch.client.Client _client;
	
	public Client() {
		this._node = nodeBuilder().client(true).node();
		this._client = this._node.client();
	}
	
	public void close() {
		this._node.close();
	}
	
	public IndexResponse index(String index, String type, String json) {
		IndexResponse response = this._client.prepareIndex(index, type)
			.setSource(json)
			.execute()
			.actionGet();
		return response;
	}
	
}
