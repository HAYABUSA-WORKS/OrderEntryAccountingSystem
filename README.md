# 飲食店のオーダーエントリー会計システム
## ○ 制作期間
2025年8月20日～28日
## ○ 使用技術
PHP　MySQL　Java　AndroidStudio(API26“Oreo” Android 8.0)　XAMPP(Apache)
## ○ 概要
訓練の最終応用課題として飲食店のオーダーエントリー会計システムを作成した。Android端末（HUAWEI JDN2-W09）を店のスタッフが所持しており、クライアント端末としてそこから無線でwebサーバにアクセスする。webサーバではPHPを用いてDBへアクセスする。  

<img width="399" height="453" alt="image" src="https://github.com/user-attachments/assets/b7def1b7-a50e-4900-b0c8-31af6fc8b521" />
<img width="586" height="606" alt="image" src="https://github.com/user-attachments/assets/c253745b-1c82-486d-adb9-bfaf7072fdea" />

## ○ テーブル詳細
【席】seats  
| 項目名 | 型 | 説明 |
| --- | --- | --- |
| s_id | CHAR(3) PRIMARY KEY | 席番号 |
| s_capacity | INT | 収容人数 |

【食品】foods  
| 項目名 | 型 | 説明 |
| --- | --- | --- |
| f_id | CHAR(5) PRIMARY KEY | 食品番号（先頭1桁 0:飲み物 1～:食べ物） |
| f_name | VARCHAR(64) | 食品名 |
| f_price | INT | 食品価格 |

【注文基本】order_basic
| 項目名 | 型 | 説明 |
| --- | --- | --- |
| o_id | INT AUTO_INCREMENT PRIMARY KEY | 注文番号 |
| o_date | DATETIME | 注文（飲食開始）日時 |
| o_s_id | CHAR(3) | 席番号（席テーブルを参照 |
| o_state | INT NOT NULL DEFAULT 0 | 会計状態（0:未払い 1:支払い済） |

【注文詳細】order_detail
| 項目名 | 型 | 説明 |
| --- | --- | --- |
| od_id | INT AUTO_INCREMENT PRIMARY KEY | 注文詳細番号 |
| od_o_id | INT | 注文番号（注文基本テーブルを参照） |
| od_f_id | CHAR(5) | 食品番号（食品テーブルを参照） |
| od_quantity | INT | 数量 |
| od_memo | VARCHAR(64) | 備考（焼き加減、味付け、温度など） |
| od_time | DATETIME | 注文日時 |
| od_state | INT NOT NULL DEFAULT 0 | 注文状態（0:なし 1:調理中 2:調理済 3:配膳済） |
