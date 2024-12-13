package com.damuzee.boot.spec.jackson.utils.fastjson;

import com.damuzee.boot.spec.jackson.utils.JSONUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JSONArray extends JSON implements List<Object>, Serializable {

    private final List<Object> list;

    public JSONArray() {
        this.list = new ArrayList<Object>();
    }

    public JSONArray(List<Object> list) {
        this.list = new ArrayList<>(list);
    }

    public JSONObject getJSONObject(int index) {
        Object value = list.get(index);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        return parseObject(value);
    }

    public JSONArray getJSONArray(int index) {
        Object value = list.get(index);
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        return parseArray(value);
    }

    public <T> List<T> toJavaList(Class<T> clazz) {
        List<T> list = new ArrayList<T>(this.size());

        for (Object item : this) {
            T classItem = JSONUtil.parseObject(item, clazz);
            list.add(classItem);
        }
        return list;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<Object> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    public boolean add(Object e) {
        return list.add(e);
    }

    public JSONArray fluentAdd(Object e) {
        list.add(e);
        return this;
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public JSONArray fluentRemove(Object o) {
        list.remove(o);
        return this;
    }

    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    public boolean addAll(Collection<? extends Object> c) {
        return list.addAll(c);
    }

    public JSONArray fluentAddAll(Collection<? extends Object> c) {
        list.addAll(c);
        return this;
    }

    public boolean addAll(int index, Collection<? extends Object> c) {
        return list.addAll(index, c);
    }

    public JSONArray fluentAddAll(int index, Collection<? extends Object> c) {
        list.addAll(index, c);
        return this;
    }

    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    public JSONArray fluentRemoveAll(Collection<?> c) {
        list.removeAll(c);
        return this;
    }

    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    public JSONArray fluentRetainAll(Collection<?> c) {
        list.retainAll(c);
        return this;
    }

    public void clear() {
        list.clear();
    }

    public JSONArray fluentClear() {
        list.clear();
        return this;
    }

    public Object set(int index, Object element) {
        if (index == -1) {
            list.add(element);
            return null;
        }

        if (list.size() <= index) {
            for (int i = list.size(); i < index; ++i) {
                list.add(null);
            }
            list.add(element);
            return null;
        }

        return list.set(index, element);
    }

    public JSONArray fluentSet(int index, Object element) {
        set(index, element);
        return this;
    }

    public void add(int index, Object element) {
        list.add(index, element);
    }

    public JSONArray fluentAdd(int index, Object element) {
        list.add(index, element);
        return this;
    }

    public Object remove(int index) {
        return list.remove(index);
    }

    public JSONArray fluentRemove(int index) {
        list.remove(index);
        return this;
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    public ListIterator<Object> listIterator(int index) {
        return list.listIterator(index);
    }

    public List<Object> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public Object get(int index) {
        return list.get(index);
    }
}
