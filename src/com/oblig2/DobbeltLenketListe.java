//Skrevet av Salem Hamidi - S333946
package com.oblig2;

////////////////// class DobbeltLenketListe //////////////////////////////

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

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
        endringer = 0;
    }

    private Node<T> finnNode(int indeks) {
        Node<T> p;
        //Hvis indeks er mindre enn antall del på 2 start på venstre side
        if (indeks < antall / 2) {
            p = hode;
            for(int i = 0; i < indeks; i++) {
                p = p.neste;
            }
        }
        else {
            //Hvis indeks er større enn antall del på 2 start på høyre side
            p = hale;
            for(int i = 0; i < antall - indeks - 1; i++) {
                p = p.forrige;
            }
        }
        return p;
    }

    public DobbeltLenketListe(T[] a) {
        if(a == null) {
           Objects.requireNonNull(a, "Tabell a er null");
        }
        for (int i = 0; i < a.length; i++) {
            if (hode == null) {
                if (a[i] != null) {
                    hode = new Node<>(a[i], null, hale);
                    hale = hode;
                    antall++;
                }
            }
            else {
                if (a[i] != null) {
                    hale.neste = new Node<>(a[i], hale, null);
                    hale = hale.neste;
                    antall++;
                }
            }
        }
    }

    public Liste<T> subliste(int fra, int til) {
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


    @Override
    public boolean leggInn(T verdi) {
       Objects.requireNonNull(verdi, "Tabell a er null");
        if(antall > 0) {
            //Hvis man skal legg inn en ny node i enden
            Node<T> n = new Node<>(verdi);
            hale.neste = n;
            n.forrige = hale;
            hale = n;

        } else {
            //Hvis man skal legg inn en ny node i begynnelsen
            Node<T> n = new Node<>(verdi);
            hode = n;
            hale = n;
        }
        endringer++;
        antall++;
        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Ikke tillatt med null-verdier!");
        indeksKontroll(indeks, true);

        if(antall == 0) {
            leggInn(verdi);
            return;
        }
        //Oppretter ny hode
        else if(indeks == 0){
            Node<T> n = new Node<>(verdi);
            n.neste = hode;
            hode.forrige = n;
            hode = n;
        }
        //Oppretter ny hale
        else if(indeks == antall){
            Node<T> n = new Node<>(verdi);
            n.forrige = hale;
            hale.neste = n;
            hale = n;
        }
        //Dersom indeks er større enn antall/2 start fra høyre
        else if(indeks > antall/2){
            Node<T> aktuell = hode;
            for(int i = 0; i < indeks-1; i++){
                aktuell = aktuell.neste;
            }

            Node<T> p = new Node<>(verdi);
            p.neste = aktuell.neste;
            p.forrige = aktuell;
            aktuell.neste = p;
            p.neste.forrige = p;
        }
        //Dersom indeks er mindre enn antall/2 start fra venstre
        else{
            Node<T> q = hale;
            for(int i = antall; i > indeks; i--){
                q = q.forrige;
            }

            Node<T> n = new Node<>(verdi);
            n.neste = q.neste;
            n.forrige = q;
            q.neste = n;
            n.neste.forrige = n;
        }
        antall++;
        endringer++;
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
        Node<T> p = hode;
        Node<T> q = null;

        while (p != null) {
            q = p.neste;
            p.neste = null;
            p.verdi = null;
            p = q;
        }
        hode = hale = null;
        antall = 0;
        endringer++;
    }

    @Override
    public String toString() {
        if (tom()) {
            return "[]";
        }

        StringBuilder s = new StringBuilder();
        s.append("[");
        Node<T> p = hode;
        while(p != null) {
            if (p == hale) {
                s.append(p.verdi);
            } else {
                s.append(p.verdi + ", ");
            }
            p = p.neste;
        }
        s.append(']');
        return s.toString();
    }

    public String omvendtString(){
        if (tom()) {
            return "[]";
        }

        StringBuilder s = new StringBuilder();
        s.append("[");
        Node<T> q = hale;

        while(q != null) {
            if (q == hode) {
                s.append(q.verdi);
            } else {
                s.append(q.verdi + ", ");
            }
            q = q.forrige;
        }
        s.append(']');
        return s.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false);
        return new DobbeltLenketListeIterator(indeks);
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
            indeksKontroll(indeks, false);
            denne = finnNode(indeks);
            fjernOK = false;
            iteratorendringer = endringer;
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next(){
            if(iteratorendringer != endringer) {
                throw new ConcurrentModificationException();
            }
            if(!hasNext()) {
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

            if(antall == 0) {
                return;
            }

            if(antall == 1) {
                hode = null;
                hale = null;
            }

            else if(denne == null) {
                hale = hale.forrige;
                hale.neste = null;
            }
            else if(denne.forrige == hode) {
                hode = denne;
                denne.forrige = null;
            }
            else {
                denne.forrige.forrige.neste = denne;
                denne.forrige = denne.forrige.forrige;
            }
            fjernOK = false;
            antall--;
            endringer++;
            iteratorendringer++;
        }
    }  // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        for(int i = 0; i < liste.antall(); i++) {
            for(int j = 0; j < liste.antall() - i-1; j++) {
                if(c.compare(liste.hent(j), liste.hent(j + 1)) > 0) {
                    T a = liste.hent(j);
                    T b = liste.hent(j + 1);
                    liste.oppdater(j, b);
                    liste.oppdater(j + 1, a);
                }
            }
        }
    }
} // class DobbeltLenketListe
