package com.cursoandroid.flippybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class FlippyBird extends ApplicationAdapter {
	private int contador=0;

	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture GameOver;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Random Nmrandomico;
	private BitmapFont fonte;
	private BitmapFont mensagem;
	private Circle passaroCirculo;
	private Rectangle RetangulocanoTopo;
	private Rectangle RetangulocanoBaixo;
	/*private ShapeRenderer shape;*/


    private int larguraDispositivo;
    private int alturaDispositivo;
    private int estadoGame = 0; // estado inicial
    private int pontuacao=0;


    private float variacao =0;
    private float velocidadequeda= 0;
    private float posicaoInicialVertical=0;
    private float posicaoMovimentoCanoHoriz ;
    private float espaçoEntreCanos;
    private float deltaTime;
    private float AtraEntreCanosRandomica;

    private Boolean marcouPonto = false;
	@Override
	public void create () {
		batch = new SpriteBatch();
		passaros = new Texture[3];
		passaroCirculo = new Circle();
		/*RetangulocanoBaixo =  new Rectangle();
		RetangulocanoTopo = new Rectangle();
		shape = new ShapeRenderer();*/
		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);

		mensagem = new BitmapFont();
		mensagem.setColor(Color.WHITE);
		fonte.getData().setScale(3);


		passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");
		fundo = new Texture("fundo.png");
		canoBaixo= new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");
		GameOver = new Texture("game_over.png");


		Nmrandomico = new Random();

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
        posicaoInicialVertical= alturaDispositivo / 2;
        posicaoMovimentoCanoHoriz = larguraDispositivo - 100;
        espaçoEntreCanos = 300;

	}

	@Override
	public void render () {
		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 10;
		if (variacao > 2) variacao = 0;

		if( estadoGame == 0){
			if(Gdx.input.justTouched()){
				estadoGame = 1;
			}
		}
		else {
            velocidadequeda++;
			if (posicaoInicialVertical > 0 || velocidadequeda < 0)
				posicaoInicialVertical = posicaoInicialVertical - velocidadequeda;

		    if(estadoGame == 1){
				posicaoMovimentoCanoHoriz -= deltaTime * 200;

                if (Gdx.input.justTouched()) {
                    velocidadequeda = -15;
                }

// verica se o cano saiu interamente da tela

                if (posicaoMovimentoCanoHoriz < -canoTopo.getWidth()) {
                    posicaoMovimentoCanoHoriz = larguraDispositivo;
                    AtraEntreCanosRandomica = Nmrandomico.nextInt(400) - 200;

                    marcouPonto= false;


                }
                // verifica Pontuação
                if (posicaoMovimentoCanoHoriz < 120) {
                    if (!marcouPonto) {
                        pontuacao++;
                        marcouPonto = true;
                    }
            }

            }else{
		    	if(Gdx.input.justTouched()){
		    		estadoGame = 0;
		    		pontuacao = 0;
		    		velocidadequeda=0;
		    		posicaoInicialVertical = alturaDispositivo /2;
		    		posicaoMovimentoCanoHoriz = larguraDispositivo / 2;
				}
		    	//tela de game over

			}



        }
		batch.begin();

		batch.draw(fundo,0,0, larguraDispositivo , alturaDispositivo );
		batch.draw(canoTopo,posicaoMovimentoCanoHoriz,(alturaDispositivo / 2) + espaçoEntreCanos / 2 + AtraEntreCanosRandomica );
		batch.draw(canoBaixo,posicaoMovimentoCanoHoriz, alturaDispositivo / 2 - canoBaixo.getHeight() - espaçoEntreCanos / 2 + AtraEntreCanosRandomica);
		batch.draw(passaros[ (int) variacao ],120,posicaoInicialVertical);
		fonte.draw(batch, String.valueOf(pontuacao),larguraDispositivo /2,alturaDispositivo -50);

		if(estadoGame == 2){
			batch.draw(GameOver,larguraDispositivo/2 - GameOver.getWidth()/2,alturaDispositivo/2);
			mensagem.draw(batch," Toque para Reniciar ",larguraDispositivo/2 - 70
					,alturaDispositivo/2 - GameOver.getHeight() / 2);
		}
		batch.end();

		// desenhar formas
			passaroCirculo.set(120 + passaros[ 0 ].getWidth()/2, posicaoInicialVertical +  passaros[ 0 ].getHeight()/2,passaros[ 0 ].getWidth()/2);
			RetangulocanoBaixo = new Rectangle(posicaoMovimentoCanoHoriz,alturaDispositivo / 2 - canoBaixo.getHeight() - espaçoEntreCanos / 2 + AtraEntreCanosRandomica,
					canoBaixo.getWidth(),canoBaixo.getHeight());
			RetangulocanoTopo = new Rectangle(posicaoMovimentoCanoHoriz,(alturaDispositivo / 2) + espaçoEntreCanos / 2 + AtraEntreCanosRandomica,canoTopo.getWidth(),
					canoTopo.getHeight());

			/*shape.begin(ShapeRenderer.ShapeType.Filled);
			shape.circle(passaroCirculo.x,passaroCirculo.y,passaroCirculo.radius);
			shape.rect(RetangulocanoBaixo.x,RetangulocanoBaixo.y,RetangulocanoBaixo.width,RetangulocanoBaixo.height);
			shape.rect(RetangulocanoTopo.x,RetangulocanoTopo.y,RetangulocanoTopo.width,RetangulocanoTopo.height);
			shape.setColor(Color.RED);

			shape.end();*/

			// teste de colisão

			if(Intersector.overlaps(passaroCirculo,RetangulocanoBaixo ) || Intersector.overlaps(passaroCirculo,RetangulocanoTopo) || posicaoInicialVertical <=0 || posicaoInicialVertical>=alturaDispositivo){
				Gdx.app.log("colisão","houve colisão");
				estadoGame = 2;
			}

	}
	

}
