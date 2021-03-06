package com.tinkerpop.rexster;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Query;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.DefaultQuery;
import com.tinkerpop.gremlin.pipes.filter.LabelFilterPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.blueprints.util.VerticesFromEdgesIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.util.MultiIterable;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class MockVertex implements Vertex {

    private String id;

    private Hashtable<String, Object> properties = new Hashtable<String, Object>();

    private ArrayList<Edge> inEdges = new ArrayList<Edge>();
    private ArrayList<Edge> outEdges = new ArrayList<Edge>();

    public MockVertex(String id) {
        this.id = id;
    }

    public void setInEdges(ArrayList<Edge> inEdges) {
        this.inEdges = inEdges;
    }

    public void setOutEdges(ArrayList<Edge> outEdges) {
        this.outEdges = outEdges;
    }

    public Object getId() {
        return this.id;
    }

    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    public Set<String> getPropertyKeys() {
        return this.properties.keySet();
    }

    public Object removeProperty(String key) {
        return this.properties.remove(key);
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    private Iterable<Edge> getOutEdges(String... labels) {
        if (labels.length == 0) {
            return this.outEdges;
        } else {
            Pipe pipe = new LabelFilterPipe(labels[0], FilterPipe.Filter.EQUAL);
            pipe.setStarts(this.outEdges);
            return pipe;
        }
    }

    private Iterable<Edge> getInEdges(String... labels) {
        if (labels.length == 0) {
            return this.inEdges;
        } else {
            Pipe pipe = new LabelFilterPipe(labels[0], FilterPipe.Filter.EQUAL);
            pipe.setStarts(this.inEdges);
            return pipe;
        }
    }

    public Iterable<Edge> getEdges(final Direction direction, final String... labels) {
        if (direction.equals(Direction.OUT)) {
            return this.getOutEdges(labels);
        } else if (direction.equals(Direction.IN))
            return this.getInEdges(labels);
        else {
            return new MultiIterable<Edge>(Arrays.asList(this.getInEdges(labels), this.getOutEdges(labels)));
        }
    }

    public Iterable<Vertex> getVertices(final Direction direction, final String... labels) {
        return new VerticesFromEdgesIterable(this, direction, labels);
    }


    public Query query() {
        return new DefaultQuery(this);
    }
}
