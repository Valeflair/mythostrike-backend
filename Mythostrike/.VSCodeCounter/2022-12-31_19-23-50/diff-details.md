# Diff Details

Date : 2022-12-31 19:23:50

Directory d:\\魔兽\\Warcraft III Frozen Throne\\Maps\\羊皮纸\\中世纪UI\\Backend\\Mythostrike\\src

Total : 76 files,  404 codes, 9 comments, 83 blanks, all 496 lines

[Summary](results.md) / [Details](details.md) / [Diff Summary](diff.md) / Diff Details

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [src/Core/Card.java](/src/Core/Card.java) | Java | -38 | 0 | -10 | -48 |
| [src/Core/CardData.java](/src/Core/CardData.java) | Java | -61 | -4 | -19 | -84 |
| [src/Core/CardDeck.java](/src/Core/CardDeck.java) | Java | -14 | 0 | -5 | -19 |
| [src/Core/CardList.java](/src/Core/CardList.java) | Java | -28 | 0 | -5 | -33 |
| [src/Core/CardSpace.java](/src/Core/CardSpace.java) | Java | -24 | 0 | -5 | -29 |
| [src/Core/CardSymbol.java](/src/Core/CardSymbol.java) | Java | -4 | 0 | -2 | -6 |
| [src/Core/CardType.java](/src/Core/CardType.java) | Java | -4 | 0 | -2 | -6 |
| [src/Core/Champion.java](/src/Core/Champion.java) | Java | -33 | 0 | -11 | -44 |
| [src/Core/ChampionData.java](/src/Core/ChampionData.java) | Java | -40 | 0 | -14 | -54 |
| [src/Core/Game.java](/src/Core/Game.java) | Java | -66 | -6 | -19 | -91 |
| [src/Core/GameController.java](/src/Core/GameController.java) | Java | -204 | -53 | -51 | -308 |
| [src/Core/Identity.java](/src/Core/Identity.java) | Java | -4 | 0 | -2 | -6 |
| [src/Core/Mode.java](/src/Core/Mode.java) | Java | -15 | 0 | -7 | -22 |
| [src/Core/Phase.java](/src/Core/Phase.java) | Java | -10 | 0 | -3 | -13 |
| [src/Core/Player.java](/src/Core/Player.java) | Java | -124 | 0 | -30 | -154 |
| [src/Core/Skill.java](/src/Core/Skill.java) | Java | -40 | 0 | -18 | -58 |
| [src/Core/Skill_Trigger.java](/src/Core/Skill_Trigger.java) | Java | -17 | 0 | -7 | -24 |
| [src/Core/TriggerSkillData.java](/src/Core/TriggerSkillData.java) | Java | -52 | -1 | -12 | -65 |
| [src/Events/Event.java](/src/Events/Event.java) | Java | -31 | 0 | -10 | -41 |
| [src/Events/EventType.java](/src/Events/EventType.java) | Java | -52 | -3 | -18 | -73 |
| [src/Events/Handle/CardDrawHandle.java](/src/Events/Handle/CardDrawHandle.java) | Java | -26 | 0 | -9 | -35 |
| [src/Events/Handle/CardMoveHandle.java](/src/Events/Handle/CardMoveHandle.java) | Java | -40 | 0 | -11 | -51 |
| [src/Events/Handle/CardUseHandle.java](/src/Events/Handle/CardUseHandle.java) | Java | -20 | 0 | -7 | -27 |
| [src/Events/Handle/DamageHandle.java](/src/Events/Handle/DamageHandle.java) | Java | -48 | 0 | -15 | -63 |
| [src/Events/Handle/DamageType.java](/src/Events/Handle/DamageType.java) | Java | -11 | 0 | -5 | -16 |
| [src/Events/Handle/EventHandle.java](/src/Events/Handle/EventHandle.java) | Java | -40 | 0 | -17 | -57 |
| [src/Events/Handle/PhaseHandle.java](/src/Events/Handle/PhaseHandle.java) | Java | -18 | 0 | -6 | -24 |
| [src/Events/Listener.java](/src/Events/Listener.java) | Java | -17 | 0 | -12 | -29 |
| [src/Events/Observers/FunctionObserver.java](/src/Events/Observers/FunctionObserver.java) | Java | -15 | 0 | -9 | -24 |
| [src/Events/PhaseChangeHandle.java](/src/Events/PhaseChangeHandle.java) | Java | -15 | 0 | -4 | -19 |
| [src/Test/ChildA.java](/src/Test/ChildA.java) | Java | -9 | 0 | -3 | -12 |
| [src/Test/ChildB.java](/src/Test/ChildB.java) | Java | -3 | 0 | -2 | -5 |
| [src/Test/ChildC.java](/src/Test/ChildC.java) | Java | -7 | 0 | -4 | -11 |
| [src/Test/FatherA.java](/src/Test/FatherA.java) | Java | -5 | 0 | -3 | -8 |
| [src/Test/FatherB.java](/src/Test/FatherB.java) | Java | -3 | 0 | -2 | -5 |
| [src/Test/FatherC.java](/src/Test/FatherC.java) | Java | -11 | 0 | -4 | -15 |
| [src/Test/Main.java](/src/Test/Main.java) | Java | -2 | 0 | 2 | 0 |
| [src/Test/Runner.java](/src/Test/Runner.java) | Java | -15 | -4 | -4 | -23 |
| [src/core/Card.java](/src/core/Card.java) | Java | 47 | 0 | 13 | 60 |
| [src/core/CardData.java](/src/core/CardData.java) | Java | 112 | 5 | 16 | 133 |
| [src/core/CardDeck.java](/src/core/CardDeck.java) | Java | 14 | 0 | 5 | 19 |
| [src/core/CardList.java](/src/core/CardList.java) | Java | 27 | 0 | 5 | 32 |
| [src/core/CardSpace.java](/src/core/CardSpace.java) | Java | 24 | 0 | 5 | 29 |
| [src/core/CardSymbol.java](/src/core/CardSymbol.java) | Java | 4 | 0 | 2 | 6 |
| [src/core/CardType.java](/src/core/CardType.java) | Java | 4 | 0 | 2 | 6 |
| [src/core/Champion.java](/src/core/Champion.java) | Java | 33 | 0 | 11 | 44 |
| [src/core/ChampionData.java](/src/core/ChampionData.java) | Java | 40 | 0 | 14 | 54 |
| [src/core/Game.java](/src/core/Game.java) | Java | 66 | 6 | 21 | 93 |
| [src/core/Identity.java](/src/core/Identity.java) | Java | 4 | 0 | 2 | 6 |
| [src/core/Mode.java](/src/core/Mode.java) | Java | 18 | 0 | 8 | 26 |
| [src/core/Phase.java](/src/core/Phase.java) | Java | 10 | 0 | 3 | 13 |
| [src/core/Player.java](/src/core/Player.java) | Java | 124 | 0 | 30 | 154 |
| [src/core/Skill.java](/src/core/Skill.java) | Java | 40 | 0 | 18 | 58 |
| [src/core/Skill_Trigger.java](/src/core/Skill_Trigger.java) | Java | 17 | 0 | 7 | 24 |
| [src/core/TriggerSkillData.java](/src/core/TriggerSkillData.java) | Java | 60 | 1 | 13 | 74 |
| [src/core/management/CardManager.java](/src/core/management/CardManager.java) | Java | 52 | 6 | 9 | 67 |
| [src/core/management/EventManager.java](/src/core/management/EventManager.java) | Java | 176 | 0 | 41 | 217 |
| [src/core/management/GameController.java](/src/core/management/GameController.java) | Java | 40 | 0 | 6 | 46 |
| [src/core/management/GameManager.java](/src/core/management/GameManager.java) | Java | 147 | 43 | 38 | 228 |
| [src/core/management/PlayerManager.java](/src/core/management/PlayerManager.java) | Java | 27 | 3 | 9 | 39 |
| [src/events/Event.java](/src/events/Event.java) | Java | 30 | 0 | 9 | 39 |
| [src/events/EventType.java](/src/events/EventType.java) | Java | 45 | 14 | 20 | 79 |
| [src/events/FunctionObserver.java](/src/events/FunctionObserver.java) | Java | 12 | 0 | 8 | 20 |
| [src/events/Listener.java](/src/events/Listener.java) | Java | 16 | 0 | 11 | 27 |
| [src/events/PhaseChangeHandle.java](/src/events/PhaseChangeHandle.java) | Java | 15 | 0 | 4 | 19 |
| [src/events/handle/AttackHandle.java](/src/events/handle/AttackHandle.java) | Java | 65 | 0 | 19 | 84 |
| [src/events/handle/CardAskHandle.java](/src/events/handle/CardAskHandle.java) | Java | 59 | 2 | 16 | 77 |
| [src/events/handle/CardDrawHandle.java](/src/events/handle/CardDrawHandle.java) | Java | 26 | 0 | 9 | 35 |
| [src/events/handle/CardMoveHandle.java](/src/events/handle/CardMoveHandle.java) | Java | 40 | 0 | 11 | 51 |
| [src/events/handle/CardUseHandle.java](/src/events/handle/CardUseHandle.java) | Java | 41 | 0 | 13 | 54 |
| [src/events/handle/DamageHandle.java](/src/events/handle/DamageHandle.java) | Java | 48 | 0 | 15 | 63 |
| [src/events/handle/DamageType.java](/src/events/handle/DamageType.java) | Java | 11 | 0 | 5 | 16 |
| [src/events/handle/EventHandle.java](/src/events/handle/EventHandle.java) | Java | 40 | 0 | 17 | 57 |
| [src/events/handle/PhaseHandle.java](/src/events/handle/PhaseHandle.java) | Java | 18 | 0 | 6 | 24 |
| [src/events/handle/PlayerAskHandle.java](/src/events/handle/PlayerAskHandle.java) | Java | 9 | 0 | 3 | 12 |
| [src/events/handle/PlayerHandle.java](/src/events/handle/PlayerHandle.java) | Java | 9 | 0 | 4 | 13 |

[Summary](results.md) / [Details](details.md) / [Diff Summary](diff.md) / Diff Details