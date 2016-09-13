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

import java.util.ArrayList;
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
    ArrayList<Sprite> arrEnemigos;

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
            ImagenFondo.runAction(ScaleBy.action(0.01f, 3.0f,4.0f));
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
            arrEnemigos= new ArrayList<Sprite>();

            TimerTask TareaVerificarImpactos= new TimerTask() {
                @Override
                public void run() {
                    DetectarColisiones();
                }
            };
            Timer RelojVerificarImpactos= new Timer();
            RelojVerificarImpactos.schedule(TareaVerificarImpactos, 0, 100);

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

        void DetectarColisiones()
        {
            boolean HuboColision= false;
            for (Sprite UnEnemigoAVerificar:arrEnemigos)
            {
                if (InterseccionEntreSprites(NaveJugador, UnEnemigoAVerificar))
                {
                    HuboColision= true;
                }
            }
            if(HuboColision)
            {
                Log.d("Colision", "Hubo colision");
            }

        }
        void PonerUnEnemigo()
        {
            float AnchoEnemigo, AlturaEnemigo;
            NaveEnemigo= Sprite.sprite("enemigo.gif");
            CCPoint PosicionInicial= new CCPoint();

            AlturaEnemigo= NaveEnemigo.getHeight();
            AnchoEnemigo= NaveEnemigo.getWidth();
            Random GeneradorDeAzar= new Random();
            int hola= 1;

            PosicionInicial.y=PantallaDelDispositivo.height + AlturaEnemigo/2;
            PosicionInicial.x=GeneradorDeAzar.nextInt((int)PantallaDelDispositivo.width - (int)AnchoEnemigo) + AnchoEnemigo/2;
            NaveEnemigo.setPosition(PosicionInicial.x, PosicionInicial.y);

            CCPoint PosicionFinal= new CCPoint();


            PosicionFinal.x=PosicionInicial.x;
            PosicionFinal.y=-100;
            NaveEnemigo.runAction(MoveTo.action(3,PosicionFinal.x, PosicionFinal.y));
            super.addChild(NaveEnemigo);
            arrEnemigos.add(NaveEnemigo);

            // NaveEnemigo.runAction(RotateTo.action(0.01f, 180f));

        }


        boolean EstaEntre(int NumeroAComparar, int NumeroMenor, int NumeroMayor)
        {
            boolean devolver;
            if (NumeroMenor>NumeroMayor)
            {
                int Auxuliar= NumeroMayor;
                NumeroMayor= NumeroMenor;
                NumeroMenor= Auxuliar;
            }

            if (NumeroAComparar>=NumeroMenor && NumeroAComparar<=NumeroMayor)
            {
                devolver= true;
            }
            else {devolver= false;}

            return  devolver;
        }
        boolean InterseccionEntreSprites(Sprite Sprite1, Sprite Sprite2)
        {
            boolean Devolver;

            Devolver=false;

            int Sprite1Izquierda, Sprite1Derecha, Sprite1Abajo, Sprite1Arriba;

            int Sprite2Izquierda, Sprite2Derecha, Sprite2Abajo, Sprite2Arriba;

            Sprite1Izquierda=(int) (Sprite1.getPositionX() - Sprite1.getWidth()/2);

            Sprite1Derecha=(int) (Sprite1.getPositionX() + Sprite1.getWidth()/2);

            Sprite1Abajo=(int) (Sprite1.getPositionY() - Sprite1.getHeight()/2);

            Sprite1Arriba=(int) (Sprite1.getPositionY() + Sprite1.getHeight()/2);

            Sprite2Izquierda=(int) (Sprite2.getPositionX() - Sprite2.getWidth()/2);

            Sprite2Derecha=(int) (Sprite2.getPositionX() + Sprite2.getWidth()/2);

            Sprite2Abajo=(int) (Sprite2.getPositionY() - Sprite2.getHeight()/2);

            Sprite2Arriba=(int) (Sprite2.getPositionY() + Sprite2.getHeight()/2);



        //Borde izq y borde inf de Sprite 1 está dentro de Sprite 2

            if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) &&
            EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {

            Devolver=true;

        }

        //Borde izq y borde sup de Sprite 1 está dentro de Sprite 2

            if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) &&
            EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {

            Devolver=true;

        }

        //Borde der y borde sup de Sprite 1 está dentro de Sprite 2

            if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) &&

            EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {


            Devolver=true;

        }

        //Borde der y borde inf de Sprite 1 está dentro de Sprite 2

            if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) &&

            EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {

            Devolver=true;

        }

        //Borde izq y borde inf de Sprite 2 está dentro de Sprite 1

            if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) &&

            EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {

            Devolver=true;

        }

        //Borde izq y borde sup de Sprite 1 está dentro de Sprite 1

            if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) &&

            EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {

           Devolver=true;

        }

        //Borde der y borde sup de Sprite 2 está dentro de Sprite 1

            if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) &&

            EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {


            Devolver=true;

        }

            //Borde der y borde inf de Sprite 2 está dentro de Sprite 1

            if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) &&

            EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {

            Devolver=true;

        }

            return Devolver;
        }
    }
}
