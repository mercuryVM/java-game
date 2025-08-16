# Java Game — rework OO do shoot ‘em up

Projeto acadêmico da disciplina **ACH2003 - Computação Orientada a Objetos (USP, 2025)**.  
Refatoração em Java de um shoot ’em up vertical para um design orientado a objetos, adicionando **fases configuráveis**, **chefes**, **power-ups** e uma **mini engine** simples pra entidades e eventos.

O código-base vem daquele jogo procedural clássico (com `Main.java` gigante) e aqui foi reorganizado em classes bem encapsuladas e coleções Java no lugar de arrays fixos, além de ler fases de arquivos externos.

---

## 🎮 Como jogar

- **Movimento:** setas do teclado (ou WASD, se habilitado no código).
- **Tiro:** barra de espaço.
- **Pausa/Sair:** `P`/`Esc` (se mapeado).
- **Objetivo:** sobreviver, derrotar o **chefe** da fase, avançar.

> Dica: power-ups aparecem conforme a configuração da fase; colete pra buffar tiro, cadência, velocidade, etc.

---

## 🧱 Estrutura do projeto

```
java-game/
├─ src/                       # código-fonte (engine + jogo, inclui GameLib)
├─ gameConfig.xml             # configurações globais do jogo (vidas, lista de fases)
├─ Scene1Config.{txt,xml}     # exemplos de fase
├─ Scene2Config.{txt,xml}
└─ out/                       # artefatos de build (IDE)
```

---

## ⚙️ Requisitos

- **JDK 17** (recomendado) ou **JDK 11+**
- Qualquer IDE Java (IntelliJ/VS Code/Eclipse) **ou** CLI (`javac/java`)

---

## ▶️ Rodando

### Opção A — IntelliJ IDEA
1. **File → Open…** e selecione a pasta do repositório.
2. Certifique-se que o SDK do projeto é o **JDK 17**.
3. Rode a classe `Main` (ou a classe launcher definida no `src/`).

### Opção B — Linha de comando
```bash
# dentro da raiz do repo
javac -d out ./src/**/*.java
java -cp out Main
```

---

## 🧩 Conceitos de OO aplicados

- **Entidades com encapsulamento** (`Player`, `Enemy`, `Boss`, `Projectile`, `PowerUp`).
- **Polimorfismo de comportamento** (ex.: `Enemy` → `EnemyType1`, `EnemyType2`; `Boss` → `BossA`, `BossB`).
- **Composição de sistemas** (spawner, física/colisão, render, input).
- **Coleções dinâmicas** (`List`, `Map`, `Queue`) no lugar de arrays fixos.
- **Separação de camadas** (loop do jogo, lógica, IO de config).
- **Eventos** (spawn, hit, death, pickup).

---

## 🗺️ Sistema de fases (arquivos de configuração)

O jogo lê **um arquivo global** e **um arquivo por fase**.

### `gameConfig.xml`
Define:
- **Pontos de vida iniciais do jogador**
- **Número de fases**
- **Arquivos de configuração de cada fase**

Exemplo simplificado:

```xml
<Game>
  <PlayerLives>3</PlayerLives>
  <Stages>
    <Stage file="Scene1Config.xml"/>
    <Stage file="Scene2Config.xml"/>
  </Stages>
</Game>
```

### `SceneXConfig.xml` (ou `.txt` equivalente)
Cada linha/elemento agenda aparições:

- **INIMIGO**: tipo, quando (ms desde início da fase), posição (x,y)
- **CHEFE**: tipo, vida, quando, posição
- **POWERUP**: tipo, quando, posição

Formato conceitual (texto):

```
INIMIGO <TIPO> <QUANDO_MS> <X> <Y>
CHEFE   <TIPO> <VIDA> <QUANDO_MS> <X> <Y>
POWERUP <TIPO> <QUANDO_MS> <X> <Y>
```

Regras:
- Chefe **aparece uma única vez** por fase e **não sai da tela**.
- Derrotar o chefe ⇒ **avança** para a próxima fase.
- Power-ups: implementar **dois tipos diferentes** (efeitos à escolha).

---

## 👾 Chefes

- **2 chefes distintos**, cada um com:
  - **Padrões de movimento/ataque próprios**
  - **Barra de vida** na tela enquanto ativo
  - **Comportamento de confinamento** (não saem da área do jogo)
- A morte do chefe **encerra a fase** e carrega a próxima.

---

## 🔋 Power-ups

Exemplos possíveis:
- **FireRateUp**: aumenta cadência/espalhamento
- **SpeedUp**: buff de velocidade/manobrabilidade

---

## 📄 Licença

Projeto acadêmico para fins educacionais na disciplina ACH2003 - Computação Orientada a Objetos (USP).
