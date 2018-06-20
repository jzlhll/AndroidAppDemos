package com.allan.statusobserver;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class LimitQueue<E> implements Queue<E> {

    /**
     * 队列长度，实例化类的时候指定
     */
    private int limit;

    protected Queue<E> queue = new LinkedList<E>();

    public LimitQueue(int limit) {
        this.limit = limit;
    }

    /**
     * 入队
     */
    @Override
    public boolean offer(E e) {
        if (queue.size() >= limit) {
            //如果超出长度,入队时,先出队
            queue.poll();
        }
        return queue.offer(e);
    }

    /**
     * 出队
     */
    @Override
    public E poll() {
        return queue.poll();
    }

    /**
     * 获取队列
     *
     * @return
     * @author SHANHY
     * @date 2015年11月9日
     */
    public Queue<E> getQueue() {
        return queue;
    }

    /**
     * 获取限制大小
     *
     * @return
     * @author SHANHY
     * @date 2015年11月9日
     */
    public int getLimit() {
        return limit;
    }

    @Override
    public boolean add(E e) {
        return queue.add(e);
    }

    @Override
    public E element() {
        return queue.element();
    }

    @Override
    public E peek() {
        return queue.peek();
    }

    @Override
    public boolean isEmpty() {
        return queue.size() == 0 ? true : false;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public E remove() {
        return queue.remove();
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return queue.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return queue.retainAll(collection);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return queue.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> collection) {
        return queue.addAll(collection);
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return queue.toArray(ts);
    }
}
