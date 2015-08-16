package org.gdejohn.twelvefold;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.min;
import static java.util.Collections.emptySet;
import static java.util.stream.IntStream.range;
import static java.util.stream.LongStream.rangeClosed;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Static methods for generating combinations.
 * 
 * @author Griffin DeJohn
 */
public class Combinations {
    /**
     * The binomial coefficient <i>C({@code n}, {@code k})</i>.
     * 
     * Also the number of ways to choose {@code k} elements from a set of
     * {@code n} elements.
     */
    public static long choose(long k, long n) {
        return rangeClosed(1, k).reduce(1, (product, i) -> product * (n + 1 - i) / i);
    }
    
    /**
     * All {@code k}-combinations of the set {@code N}.
     * 
     * A <i>k</i>-combination of a set <i>N</i> is a subset of <i>k</i>
     * elements of <i>N</i>. If <i>N</i> has <i>n</i> elements, then the
     * number of <i>k</i>-combinations of <i>N</i> is equal to the
     * {@link Combinations#choose(long, long) binomial coefficient}
     * <i>C(n, k)</i>.
     */
    public static <T> Set<Set<T>> choose(int k, Set<T> N) {
        List<T> list = new ArrayList<>(N);
        
        return k < 0 || k > list.size() ? emptySet() : new AbstractSet<Set<T>>() {
            @Override
            public Iterator<Set<T>> iterator() {
                return new Iterator<Set<T>>() {
                    boolean hasNext = true;
                    
                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }
                    
                    int[] indices = range(0, k).toArray();
                    
                    @Override
                    public Set<T> next() {
                        if (!hasNext) {
                            throw new NoSuchElementException();
                        }
                        Set<T> combination = new LinkedHashSet<>();
                        for (int index : indices) {
                            combination.add(list.get(index));
                        }
                        int i = k - 1;
                        while (i >= 0 && indices[i] == list.size() - k + i) {
                            i--;
                        }
                        if (i < 0) {
                            hasNext = false;
                        }
                        else {
                            indices[i]++;
                            for (int j = indices[i]; i < k; i++, j++) {
                                indices[i] = j;
                            }
                        }
                        return combination;
                    }
                };
            }
            
            Integer size = null;
            
            @Override
            public int size() {
                return size == null ? size = (int) min(MAX_VALUE, choose(k, list.size())) : size;
            }
            
            Set<T> set = null;
            
            Set<T> set() {
                return set == null ? set = new HashSet<>(list) : set;
            }
            
            @Override
            public boolean contains(Object object) {
                if (object instanceof Set) {
                    Set<?> elements = (Set<?>) object;
                    return elements.size() == k && set().containsAll(elements);
                }
                else {
                    return false;
                }
            }
        };
    }
}
