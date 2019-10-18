//Skrevet av Salem Hamidi - S333946
package com.oblig2;

////////////////// class DobbeltLenketListe //////////////////////////////

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.SQLOutput;
import java.util.*;

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
       hode = hale = null;
        antall = 0;
    }

    private Node<T> finnNode(int indeks) {

        if (indeks < (antall / 2)) {
            Node<T> p = hale;
            for ( int i = 0; i < indeks; i++) {
                p = p.neste;
            }
            return p;
        }
        else {
            Node<T> q = hode;
            for (int i = 0; i < indeks; i++) {
                q = q.neste;
            }
            return q;
        }
    }


    public DobbeltLenketListe(T[] a) {

        DobbeltLenketListe liste = new DobbeltLenketListe();

        Objects.requireNonNull(a, "ikke tilltatt med null");

        if(a == null) {
            throw new NullPointerException("Tabellen a er null!");
        }
        if(a.length == 0) {
            return;
        }

        if(a.length == 1) {
            hode = hale;
            antall = 1;
        }

        liste.hode = new DobbeltLenketListe.Node(a);
        liste.hale = liste.hode;
        if(liste.antall() < 0) {
            tom();
        }
        DobbeltLenketListe.Node p = liste.hale;
        p.verdi = a[0];
        for (int i = 1; i < a.length; i++) {
            DobbeltLenketListe.Node q = new DobbeltLenketListe.Node(a[i]);

            q.forrige = p;
            p.neste = q;
            p = q;
            liste.hale = q;
            antall++;
        }

    }


    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(fra, til, antall);
        throw new NotImplementedException();
    }

//Oppgave1
    @Override
    public int antall() {
      return antall;
    }
//Oppgave1
    @Override
    public boolean tom() {
        return antall == 0;
    }

//Skriver ut noe men ikke alt, delvis fullført
    @Override
    public boolean leggInn(T verdi) {

        Objects.requireNonNull(verdi, "Ikke tilltatt med null-verdier");
        if(antall == 0){
            hode = hale = new Node<>(verdi);
        }
        else {
            hale = hale.neste = new Node<>(verdi);
        }
        antall++;
        endringer++;
        return true;
    }
    /*
        // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

     */
    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Ikke tilltatt med null verdier");
        indeksKontroll(indeks, true);


        if(indeks == 0) {
            hale = hode = new Node<>(verdi);
        }
        if(antall == 0){
            hale = hode;
        }

        if(indeks > antall) {
            Node<T> q = hale;
            for(int i = indeks; i < 100_001; i--) {
                q = hale.forrige;
            }
            q.forrige = new Node<>(verdi);
    }

        if(indeks < antall) {
            Node<T> p = hode;
            for(int i = indeks; i < indeks; i++) {
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
            }
            p.neste = q.neste;
        }
        antall--;
        endringer++;
        return temp;
    }

    @Override
    public void nullstill() {

        //Metode 1
        Node<T> p = hode;
        Node<T> q = null;

        while (p != null) {
            q = p.neste;
            p.neste = q;
            p.verdi = null;
            p = q;
            antall--;
        }
        //Metode 2
        for(int i = 0; i < antall; i++) {
            fjern(0);
            antall--;
        }

        hode = hale = null;
        antall = 0;
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
    /* Oppgave 10
    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    */
    public String omvendtString() {
        StringBuilder s  = new StringBuilder();
        s.append("[");

        if (!tom()) {
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
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, true);
        return iterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;
            fjernOK = false;
            iteratorendringer = endringer;
        }

        private DobbeltLenketListeIterator(int indeks){
            Node<T> p = new Node(indeks);
            p = denne;
            denne = hode;
            fjernOK = false;
            iteratorendringer = endringer;
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            if(iteratorendringer != endringer) {
                throw new ConcurrentModificationException();
            }

            if(hasNext()) {
                throw new NoSuchElementException("Ingen verdier");
            }
            fjernOK = true;
            T denneVerdi = denne.verdi;
            denne = denne.neste;
            return denneVerdi;
        }

        @Override
        public void remove(){
            if(!fjernOK) {
                throw new IllegalStateException();
            }
            if(iteratorendringer != endringer) {
                throw new ConcurrentModificationException();
            }


            fjernOK = false;
            Node<T> q = hode;
            Node<T> p = null;

            if(antall == 1) {
                hode = null;
                hale = null;
            }
            // Den første skal fjernes
            if(hode.neste == p) {
                hode = hode.neste;
                if(p == null) {
                    hale = null;
                }
                else{
                    Node<T> r = hode;
                    while (r.neste.neste != p) {
                        r = r.neste;
                    }
                    q = r.neste;
                    r.neste = p;
                    if(p == null) {
                        hale = r;
                    }
                    q.verdi = null;
                    q.neste = null;
                    antall--;
                    endringer++;
                    iteratorendringer++;
                }
            }
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