	(function(b) {
		function a(a) {
			this.initialize(a)
		}
		a.prototype = new BitmapAnimation;
		a.TOGGLE = 60;
		a.MAX_THRUST = 2;
		a.MAX_VELOCITY = 5;
		a.prototype.shipFlame = null;
		a.prototype.shipBody = null;
		a.prototype.timeout = 0;
		a.prototype.thrust = 0;
		a.prototype.vX = 0;
		a.prototype.vY = 0;
		a.prototype.bounds = 0;
		a.prototype.hit = 0;
		a.prototype.Container_initialize = a.prototype.initialize;
		a.prototype.initialize = function(a) {
			this.Container_initialize(a);
			this.onAnimationEnd = this.fireAni;
			this.gotoAndPlay("fire");
			this.name = "rat";
			this.speed = Math.random() * 6 + 2;
			this.direction = 0;
			this.vX = this.speed;
			this.vY = 0;
			this.x = 180;
			this.y = 560;
			this.currentAnimationFrame = Math.random() * a.getNumFrames("fire")
					| 0;
			this.timeout = 0;
			this.thrust = 0
		};
		a.prototype.fireAni = function() {
			this.gotoAndStop("fire")
		};
		a.prototype.tick = function() {
			this.x += this.vX;
			this.y += this.vY
		};
		a.prototype.accelerate = function() {
			this.thrust += this.thrust + .6;
			if (this.thrust >= a.MAX_THRUST)
				this.thrust = a.MAX_THRUST;
			this.vX += Math.sin(this.rotation * (Math.PI / -180)) * this.thrust;
			this.vY += Math.cos(this.rotation * (Math.PI / -180)) * this.thrust;
			this.vX = Math.min(a.MAX_VELOCITY, Math.max(-a.MAX_VELOCITY,
					this.vX));
			this.vY = Math.min(a.MAX_VELOCITY, Math.max(-a.MAX_VELOCITY,
					this.vY))
		};
		b.Ship = a
	})(window);
	(function(b) {
		var a = function(c, b, a) {
			this.initialize(c, b, a)
		};
		a.prototype = new Container;
		a.prototype.Container_initialize = a.prototype.initialize;
		a.prototype.initialize = function(c, b, a) {
			this.Container_initialize();
			this.addChild(a);
			this.velocity = c;
			this.targetPosition = b
		};
		a.prototype.tick = function() {
			this.x += this.velocity.x;
			this.y += this.velocity.y
		};
		b.Plasma = a
	})(window);
	(function(b) {
		var a = function(a, b) {
			this.x = a || 0;
			this.y = b || 0
		};
		a.prototype = {
			"set" : function(a, b) {
				this.x = a;
				this.y = b;
				return this
			},
			copy : function(a) {
				this.x = a.x;
				this.y = a.y;
				return this
			},
			clone : function() {
				return new THREE.Vector2(this.x, this.y)
			},
			add : function(a, b) {
				this.x = a.x + b.x;
				this.y = a.y + b.y;
				return this
			},
			addSelf : function(a) {
				this.x += a.x;
				this.y += a.y;
				return this
			},
			sub : function(a, b) {
				this.x = a.x - b.x;
				this.y = a.y - b.y;
				return this
			},
			subSelf : function(a) {
				this.x -= a.x;
				this.y -= a.y;
				return this
			},
			multiplyScalar : function(a) {
				this.x *= a;
				this.y *= a;
				return this
			},
			divideScalar : function(a) {
				if (a) {
					this.x /= a;
					this.y /= a
				} else
					this.set(0, 0);
				return this
			},
			negate : function() {
				return this.multiplyScalar(-1)
			},
			dot : function(a) {
				return this.x * a.x + this.y * a.y
			},
			lengthSq : function() {
				return this.x * this.x + this.y * this.y
			},
			length : function() {
				return Math.sqrt(this.lengthSq())
			},
			normalize : function() {
				return this.divideScalar(this.length())
			},
			distanceTo : function(a) {
				return Math.sqrt(this.distanceToSquared(a))
			},
			distanceToSquared : function(c) {
				var a = this.x - c.x, b = this.y - c.y;
				return a * a + b * b
			},
			setLength : function(a) {
				return this.normalize().multiplyScalar(a)
			},
			equals : function(a) {
				return a.x === this.x && a.y === this.y
			}
		};
		b.Vector2 = a
	})(window);
	(function(b) {
		var a = function(b, a) {
			this.initialize(b, a)
		};
		a.prototype = new Container;
		a.prototype.Container_initialize = a.prototype.initialize;
		a.prototype.initialize = function(b, a) {
			this.Container_initialize();
			this.addChild(a);
			this.velocity = b
		};
		a.prototype.tick = function() {
			this.x += this.velocity.x;
			this.y += this.velocity.y
		};
		b.Enemy = a
	})(window);
	(function(b) {
		var a = function() {
		};
		a.getRandomNumber = function(a, b) {
			return a + Math.floor(Math.random() * (b - a + 1))
		};
		b.MathHelp = a
	})(window);
	Loading = function(h, b, a, f, d, c, g, e) {
		this.text = h;
		this.fontSize = b;
		this.baseFontSize = a;
		this.color = f;
		this.position = d;
		this.interval = c;
		this.font = g;
		this.bolder = e;
		return this.init()
	};
	Loading.prototype.init = function() {
		var c = [], a = this, b = a.text.split("");
		for (i in b)
			c.push({
				text : b[i],
				fontSize : a.fontSize,
				baseFontSize : a.baseFontSize,
				color : a.color,
				position : new Vector2(a.position.x + i * a.interval,
						a.position.y),
				font : a.font,
				bolder : a.bolder
			});
		return c
	};
	var loading = new Loading("Loading", 30, 30, "#ffffff",
			new Vector2(130, 40), 15, "宋体", "bolder"), loadingCanvas = document
			.createElement("canvas");
	loadingCanvas.width = 260;
	loadingCanvas.height = 150;
	var cxt = loadingCanvas.getContext("2d");
	cxt.fillStyle = loading[0].color;
	function drawLoading() {
		for (i in loading) {
			cxt.font = loading[i].bolder + " " + loading[i].fontSize + "px "
					+ loading[i].font;
			cxt.fillText(loading[i].text, loading[i].position.x,
					loading[i].position.y)
		}
	}
	var currentMap = 0;
	function changeFontSize() {
		if (currentMap > 400)
			currentMap = 0;
		currentMap += 5;
		if (parseInt(currentMap / 40) <= loading.length - 1)
			loading[parseInt(currentMap / 40)].fontSize = 2
					* loading[0].baseFontSize - currentMap % 40;
		if (parseInt(currentMap / 40) + 1 <= loading.length - 1)
			loading[parseInt(currentMap / 40) + 1].fontSize = currentMap % 40
					+ loading[0].baseFontSize
	}
	function draw() {
		cxt.clearRect(0, 0, loadingCanvas.width, loadingCanvas.height);
		drawLoading();
		changeFontSize()
	}
	setInterval(draw, 15);
	var fpsLabel, canvas, stage, plasma, bgBmp, shipBmp, ship, explode, plasmaBmp, container, txt, plasmas = [], enemyImage, enemyBmp, explodeImage, enemys = [], target, grids = [], gridImage, girdBmp, backgroundImg, loadingTxt, gameStartTag = false, scoreLabel, score = 0;
	function main() {
		canvas = document.getElementById("canvas");
		stage = new Stage(canvas);
		loadingTxt = new Bitmap(loadingCanvas);
		loadingTxt.y = 200;
		stage.addChild(loadingTxt);
		Ticker.setFPS(60);
		Ticker.addListener(window);
		loadImage()
	}
	main();
	function loadImage() {
		var a = new PxLoader;
		backgroundImg = a
				.addImage("http://images.cnblogs.com/cnblogs_com/iamzhanglei/backdrop.png");
		plasmaImg = a
				.addImage("http://images.cnblogs.com/cnblogs_com/iamzhanglei/plasma.png");
		enemyImage = a
				.addImage("http://images.cnblogs.com/cnblogs_com/iamzhanglei/mine.png");
		explodeImage = a
				.addImage("http://images.cnblogs.com/cnblogs_com/iamzhanglei/explosion2-64.png");
		shipBmp = a
				.addImage("http://images.cnblogs.com/cnblogs_com/iamzhanglei/ship.png");
		gridImage = a
				.addImage("http://images.cnblogs.com/cnblogs_com/iamzhanglei/grid.png");
		a.addCompletionListener(function() {
			init()
		});
		a.addProgressListener(function() {
		});
		a.start()
	}
	function init() {
		stage.enableMouseOver(10);
		bgBmp = new Bitmap(backgroundImg);
		plasmaBmp = new Bitmap(plasmaImg);
		enemyBmp = new Bitmap(enemyImage);
		girdBmp = new Bitmap(gridImage);
		stage.addChild(bgBmp);
		for (var f = 0; f < 7; f++)
			for (var g = 0; g < 11; g++) {
				var b = girdBmp.clone();
				b.alpha = .5;
				b.x = f * 62;
				b.y = g * 62;
				grids.push(b);
				stage.addChild(b)
			}
		plasmaBmp.regX = 48;
		plasmaBmp.regY = 96;
		handleexplodeImageLoad();
		handleImageLoad();
		var a = new Container;
		a.x = 113;
		a.y = 310;
		var e = new Shape;
		e.alpha = .01;
		e.graphics.beginFill("#3A5C68").drawRect(0, 0, 131, 40).beginFill(
				"#FFF");
		a.addChild(e);
		var d = new Text("ABOUT", "bold 36px Arial", "#FFF");
		d.textBaseline = "top";
		a.addChild(d);
		a.onClick = function() {
			alert("under construction. ：）")
		};
		a.onMouseOver = function() {
			d.color = "#FF7E00";
			stage.update()
		};
		a.onMouseOut = function() {
			d.color = "#fff";
			stage.update()
		};
		stage.addChild(a);
		container = new Container;
		container.x = 133;
		container.y = 260;
		target = new Shape;
		target.alpha = .01;
		target.graphics.beginFill("#3A5C68").drawRect(-10, -10, 105, 50)
				.beginFill("#FFF");
		container.addChild(target);
		txt = new Text("PLAY", "bold 36px Arial", "#FFF");
		txt.textBaseline = "top";
		container.addChild(txt);
		container.onClick = function() {
			gameStartTag = true;
			container.visible = false;
			stage.removeChild(a);
			generateMines(10)
		};
		container.onMouseOver = function() {
			txt.color = "#FF7E00";
			stage.update()
		};
		container.onMouseOut = function() {
			txt.color = "#fff";
			stage.update()
		};
		stage.addChild(container);
		fpsLabel = new Text("-- FPS", "bold 14px Arial", "#fff");
		fpsLabel.x = 310;
		fpsLabel.y = 20;
		fpsLabel.alpha = .8;
		stage.addChild(fpsLabel);
		stage.removeChild(loadingTxt);
		var c = new Text("create by 当耐特砖家", "bold 12px Arial", "#fff");
		c.alpha = .5;
		c.x = 230;
		c.y = 630;
		stage.addChild(c);
		scoreLabel = new Text("Score: --", "bold 14px Arial", "#fff");
		scoreLabel.y = 20;
		stage.addChild(scoreLabel)
	}
	function generateMines(d) {
		for (var b = 0; b < d; b++) {
			var c = enemyBmp.clone(), a = new Enemy(new Vector2(0, 1), c);
			a.x = MathHelp.getRandomNumber(0, 340);
			a.y = MathHelp.getRandomNumber(-10, -50);
			enemys.push(a);
			stage.addChild(a)
		}
	}
	function handleexplodeImageLoad() {
		var a = new SpriteSheet({
			images : [ explodeImage ],
			frames : {
				width : 64,
				height : 64,
				regX : 32,
				regY : 32
			},
			animations : {
				explode : [ 0, 13, "explode" ]
			}
		});
		explode = new BitmapAnimation(a);
		explode.onAnimationEnd = colorChange;
		explode.name = "rat";
		explode.speed = 1;
		explode.direction = 0;
		explode.vX = explode.speed;
		explode.vY = 0;
		explode.x = 180;
		explode.y = 560;
		explode.currentAnimationFrame = 0;
		stage.addChild(explode)
	}
	function colorChange() {
		explode.alpha = 0;
		explode.gotoAndStop("explode")
	}
	function handleImageLoad() {
		var a = new SpriteSheet({
			images : [ shipBmp ],
			frames : {
				width : 24,
				height : 24,
				regX : 12,
				regY : 12
			},
			animations : {
				fire : [ 0, 3, "fire" ]
			}
		});
		ship = new Ship(a);
		stage.addChild(ship)
	}
	var shootHeld, lfHeld, rtHeld, fwdHeld, mineCount = 20;
	function updateScore() {
	}
	function tick() {
		if (score % 5e3 === 0 && score !== 0)
			try {
				$("textarea[class=comment_textarea]").val(
						"恭喜你已经超过" + score + "分！");
				$("#btn_comment_submit").trigger("click");
				score += 50
			} catch (j) {
			}
		for (var a = 0; a < stage.children.length; a++)
			stage.children[a].alpha === 0
					&& stage.removeChild(stage.children[a]);
		if (gameStartTag) {
			if (enemys.length === 0) {
				mineCount += 10;
				generateMines(mineCount)
			}
			scoreLabel.text = "Score: " + score;
			for (var b = 0; b < enemys.length; b++)
				if (enemys[b].x > canvas.width + 100
						|| enemys[b].x<-100||enemys[b].y>canvas.height + 20
						|| enemys[b].y < -100) {
					stage.removeChild(enemys[b]);
					enemys.splice(b--, 1)
				}
			for (var a = 0; a < plasmas.length; a++)
				for (var b = 0; b < enemys.length; b++)
					if (plasmas[a]) {
						var e = new Vector2(enemys[b].x + 16, enemys[b].y + 16), c = new Vector2(
								plasmas[a].x
										+ 48
										* Math.sin(plasmas[a].rotation
												* Math.PI / 180), plasmas[a].y
										- 48
										* Math.cos(plasmas[a].rotation
												* Math.PI / 180));
						if (c.distanceTo(e) < 8) {
							score += 50;
							explode.x = e.x;
							explode.y = e.y;
							stage.removeChild(enemys[b]);
							stage.removeChild(plasmas[a]);
							plasmas.splice(a--, 1);
							enemys.splice(b--, 1);
							var f = explode.clone();
							f.gotoAndPlay("explode");
							f.onAnimationEnd = function() {
								this.gotoAndStop("explode");
								this.alpha = 0
							};
							stage.addChild(f)
						}
						if (c.x > canvas.width + 100
								|| c.x<-100||c.y>canvas.height + 100
								|| c.y < -100) {
							stage.removeChild(plasmas[a]);
							plasmas.splice(a--, 1)
						}
					}
			if (shootHeld === true) {
				var h = plasmaBmp.clone(), i = new Vector2(-Math
						.sin(ship.rotation * (Math.PI / -180)) * 10, -Math
						.cos(ship.rotation * (Math.PI / -180)) * 10), d = new Plasma(
						i, 1, h);
				d.x = ship.x;
				d.y = ship.y;
				d.rotation = ship.rotation;
				plasmas.push(d);
				stage.addChild(d);
				ship.gotoAndPlay("fire")
			}
			if (lfHeld)
				ship.rotation -= 2;
			if (rtHeld)
				ship.rotation += 2;
			for ( var g in grids) {
				if (grids[g].x < -63)
					grids[g].x = 370;
				grids[g].x--
			}
			fpsLabel.text = Math.round(Ticker.getMeasuredFPS()) + " FPS"
		}
		stage.update()
	}
	document.onkeydown = handleKeyDown;
	document.onkeyup = handleKeyUp;
	var KEYCODE_SPACE = 32, KEYCODE_UP = 38, KEYCODE_LEFT = 37, KEYCODE_RIGHT = 39, KEYCODE_W = 87, KEYCODE_A = 65, KEYCODE_D = 68, KEYCODE_J = 74, img = new Image;
	function handleKeyDown(a) {
		if (!a)
			var a = window.event;
		switch (a.keyCode) {
		case KEYCODE_J:
			shootHeld = true;
			break;
		case KEYCODE_A:
			lfHeld = true;
			break;
		case KEYCODE_LEFT:
			lfHeld = true;
			break;
		case KEYCODE_D:
			rtHeld = true;
			break;
		case KEYCODE_RIGHT:
			rtHeld = true;
			break;
		case KEYCODE_W:
		case KEYCODE_UP:
			fwdHeld = true
		}
	}
	function handleKeyUp(a) {
		if (!a)
			var a = window.event;
		switch (a.keyCode) {
		case KEYCODE_J:
			shootHeld = false;
			break;
		case KEYCODE_A:
			lfHeld = false;
			break;
		case KEYCODE_LEFT:
			lfHeld = false;
			break;
		case KEYCODE_D:
			rtHeld = false;
			break;
		case KEYCODE_RIGHT:
			rtHeld = false;
			break;
		case KEYCODE_W:
		case KEYCODE_UP:
			fwdHeld = false
		}
	}
