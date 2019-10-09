//Skrevet av Salem Hamidi - S333946
package com.oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.SQLOutput;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     *
     * @param <T>
     */

    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen


    public DobbeltLenketListe() {
        //   throw new NotImplementedException();
    }

    private Node<T> finnNode(int indeks) {
        if (indeks < (antall / 2)) {
            Node<T> p = hale;
            for (int i = 0; i < indeks; i++) {
                p = p.forrige;
            }
            return p;
        }
        else {
            Node<T> p = hode;
            for (int i = 0; i < indeks; i++) {
                p = p.neste;
            }
            return p;
        }
    }


    public DobbeltLenketListe(T[] a) {
        DobbeltLenketListe liste = new DobbeltLenketListe();

        liste.hode = new DobbeltLenketListe.Node(a);
        liste.hale = liste.hode;

        DobbeltLenketListe.Node p = liste.hale;
        p.verdi = a[0];
        for (int i = 1; i < a.length; i++) {
            DobbeltLenketListe.Node q = new DobbeltLenketListe.Node(a[i]);
            q.forrige = p;
            p.neste = q;

            p = q;

            liste.hale = q;
        }

            if(a.length < 1) {
                throw new NullPointerException("Tabellen a er null!");
            }
            if(a.length == 1) {
                hode = hale;
            }
            if(a == null){
                String str = null;
                str = Objects.requireNonNull(str);
            }
        }


    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(fra, til, antall);
        throw new NotImplementedException();
    }


    @Override
    public int antall() {
      return antall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }
//Skriver ut noe men ikke alt, delvis fullført
    @Override
    public boolean leggInn(T verdi) {

        Objects.requireNonNull(verdi, "Ikke tilltatt med null-verdier");
        if(antall == 0){
            hode = new Node<>(verdi);
            hode = hale;
        }
        else {
            hale = hale.neste = new Node<>(verdi);
        }
        antall++;
        endringer++;
        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Ikke tilltatt med null verdier");
        indeksKontroll(indeks, true);

        if(indeks == 0) {
            hode = new Node<>(verdi);
        }
        if(antall == 0){
            hale = hode;
        }

        else if(indeks < antall) {
            hale = hale.forrige = new Node<>(verdi);
        }
        else {
            Node<T> p = hode;
            for(int i = 1; i < indeks; i++) {
                p = p.neste;
            }
            p.neste = new Node<>(verdi);
        }
        antall++;
    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if (verdi == null) {
            return -1;
        }

        Node<T> p = hode;

        for (int indeks = 0; indeks < antall; indeks++) {
            if (p.verdi.equals(verdi)) {
                return indeks;
            }
            p = p.neste;
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi, "Ikke tilltatt med null-verdier");
        indeksKontroll(indeks, false);

        Node<T> p = finnNode(indeks);
        T gammelVerdi = p.verdi;
        p.verdi = nyverdi;
        endringer++;
        return gammelVerdi;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) {
            return false;
        }
        Node<T> q = hode;
        Node<T> p = null;

        while (q != null) {
            if (q.verdi.equals(verdi)) {
                break;
            }
            p = q;
            q = q.neste;
        }
        if (q == null) {
            return false;
        }
        else if (q == hode) {
            hode = hode.neste;
        }

        else {
            p.neste = q.neste;
        }
        if (q == hale) {
            hale = p;
        }
        q.verdi = null;
        q.neste = null;
        antall--;
        endringer++;
        return true;
    }
    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks, false);

        T temp;

        if(indeks == 0) {
            temp = hode.verdi;
            hode = hode.neste;
            if (antall == 1) {
                hale = null;
            }
        }
        else {
            Node<T> p = finnNode(indeks - 1);
            Node<T>  q = p.neste;
            temp = q.verdi;
            if(q == hale) {
                hale = p;
                p.neste = q.neste;
            }
        }
        antall--;
        endringer++;
        return temp;
    }

    @Override
    public void nullstill() {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append('[');

        if (!tom()) {
            Node<T> p = hode;
            s.append(p.verdi);

            p = p.neste;

            while (p != null) {
                s.append(',').append(' ').append(p.verdi);
                p = p.neste;
            }
        }
        s.append(']');
        return s.toString();
    }

    public String omvendtString() {
        StringBuilder s  = new StringBuilder();
        s.append("[");
        if(!tom()) {
            Node<T> q = hale;
            s.append(q.verdi);

            q = q.forrige;

            while(q != null){
                s.append(',').append(' ').append(q.verdi);
                q = q.forrige;
            }
        }
        s.append(']');
        return s.toString();
    }

    @Override
    public Iterator<T> iterator() {
        throw new NotImplementedException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new NotImplementedException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            throw new NotImplementedException();
        }

        private DobbeltLenketListeIterator(int indeks){
            throw new NotImplementedException();
        }

        @Override
        public boolean hasNext(){
            throw new NotImplementedException();
        }

        @Override
        public T next(){
            throw new NotImplementedException();
        }

        @Override
        public void remove(){
            throw new NotImplementedException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new NotImplementedException();
    }
} // class DobbeltLenketListe

/*
 for (int i = 0; i < a.length && a[i] == null; i++) {
            if (i < a.length) {
                Node<T> p = hode = new Node<>(a[i]);
                antall = 1;
                for (i++; i < a.length; i++) {
                    if (a[i] != null) {
                        p = p.neste = new Node<>(a[i]);
                        antall++;
                    }
                }
                hale = p;
            }
 */