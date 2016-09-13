package com.example.tp2.tp5.Model;

import android.util.Log;

import org.cocos2d.actions.Scheduler;
import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.RotateTo;
import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 41587805 on 13/9/2016.
 */
public class clsJuego {

    CCGLSurfaceView _VistaDelJuego;
    CCSize PantallaDelDispositivo;
    Sprite NaveJugador, ImagenFondo, NaveEnemigo;
    Label lblTituloJuego;

    public clsJuego(CCGLSurfaceView VistaDelJuego)
    {
        _VistaDelJuego= VistaDelJuego;
    }

    public void  ComenzarJuego()
    {
        Log.d("Comenzar", "Comienza el juego");
        Director.sharedDirector().attachInView(_VistaDelJuego);
        PantallaDelDispositivo=Director.sharedDirector().displaySize();
        Director.sharedDirector().runWithScene(EscenaDelJuego());
    }

    private Scene EscenaDelJuego()
    {
        Scene EscenaADevolver=Scene.node();
        CapaDeFondo MiCapaDeFondo;
        MiCapaDeFondo= new CapaDeFondo();
        CapaDelFrente MiCapaDeFrente= new CapaDelFrente();
        EscenaADevolver.addChild(MiCapaDeFondo, -10);
        EscenaADevolver.addChild(MiCapaDeFrente,10);

        return EscenaADevolver;
    }

    class CapaDeFondo extends Layer
    {
        public CapaDeFondo()
        {
            PonerImagenFondo();
        }
        private void PonerImagenFondo()
        {
            ImagenFondo= Sprite.sprite("fondo.png");
            ImagenFondo.setPosition(PantallaDelDispositivo.width/2, PantallaDelDispositivo.height/2);
            ImagenFondo.runAction(ScaleBy.action(0.01f, 2.0f,2.0f));
            super.addChild(ImagenFondo);
        }

        private void PonerTituloJuego()
        {
            lblTituloJuego=Label.label("Bienvenido", "Arial", 30);
            float AltodelTitulo;
            AltodelTitulo=lblTituloJuego.getHeight();
            lblTituloJuego.setPosition(PantallaDelDispositivo.width/2, PantallaDelDispositivo.height=AltodelTitulo/2);
            super.addChild(lblTituloJuego);
        }
    }



    class CapaDelFrente extends Layer
    {
        public CapaDelFrente() {
            PonerNaveJugadorPosicionInicial();
            TimerTask TareaPonerEnemigos= new TimerTask() {
                @Override
                public void run() {
                    PonerUnEnemigo();
                }
            };
            Timer RelojEnemigos= new Timer();
            RelojEnemigos.schedule(TareaPonerEnemigos,0,1000);
        }

        private void PonerNaveJugadorPosicionInicial()
        {
            NaveJugador= Sprite.sprite("rocket_mini.png");
            float posicionInicialX, posicionInicialY;
            posicionInicialX=PantallaDelDispositivo.width/2;

            posicionInicialY= NaveJugador.getHeight()/2;
            NaveJugador.setPosition(posicionInicialX,posicionInicialY);
            super.addChild(NaveJugador);
        }
        void PonerUnEnemigo()
        {
            Random GeneradorDeAzar= new Random();
            NaveEnemigo= Sprite.sprite("enemigo.gif");
            CCPoint PosicionInicial= new CCPoint();

            float AlturaEnemigo= NaveEnemigo.getHeight();
            PosicionInicial.x=GeneradorDeAzar.nextInt((int)PantallaDelDispositivo.width );
            PosicionInicial.y= PantallaDelDispositivo.height + AlturaEnemigo;
            NaveEnemigo.setPosition(PosicionInicial.x, PosicionInicial.y);
            NaveEnemigo.runAction(RotateTo.action(0.01f, 180f));

            CCPoint PosicionFinal= new CCPoint();
            PosicionFinal.x=PosicionInicial.x;
            PosicionFinal.y=AlturaEnemigo/2;

           super.addChild(NaveEnemigo);
        }
    }


}
