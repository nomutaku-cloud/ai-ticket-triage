# ai-ticket-triage

Quarkus LangChain4j を使用したチケットトリアージのサンプルアプリケーションです。
問い合わせ内容（テキスト）を受け取り、LLM が必要に応じて Tool を呼び出しながら調査結果を構造化データとして返します。
既存の問い合わせ分析 API に加えて、サービス状態、類似インシデント、担当チーム連絡先を参照する調査 API を含みます。

## 前提条件

- Java 25
- OpenAI API キー

```shell
export OPENAI_API_KEY="your_api_key_here"
```

## 起動

```shell
./mvnw quarkus:dev
```

## 使用例

### 問い合わせ分析

```shell
curl -s -X POST \
  -H "Content-Type: text/plain" \
  --data-binary "Webアプリケーションにログインできません。今朝から複数の利用者で同じ問題が発生しており、画面には「Database connection timeout」と表示されています。" \
  http://localhost:8080/tickets/analyze | jq .
```

```json
{
  "summary": "複数の利用者がWebアプリケーションにログインできず、Database connection timeoutのエラーメッセージが表示されている。",
  "category": "DATABASE",
  "priority": "HIGH",
  "recommendedAction": "データベース接続の状態を確認し、必要に応じて再起動または障害調査を行う。",
  "assignedTeam": "DATABASE"
}
```

### Tool を使った問い合わせ調査

```shell
curl -s -X POST \
  -H "Content-Type: text/plain" \
  --data-binary "payment-service で Connection timeout が発生しています。サービス状態と過去の類似インシデントを調査し、担当チームの連絡先も確認してください。" \
  http://localhost:8080/tickets/investigate | jq .
```

```json
{
  "summary": "payment-service で接続タイムアウトが発生しています。",
  "category": "APPLICATION",
  "priority": "HIGH",
  "investigationResult": "payment-service の状態は DEGRADED で、外部決済ゲートウェイへのレイテンシが増加しています。過去の類似インシデントでは、接続タイムアウトが多発しており、接続プールの上限を拡張し、スロットリング設定を緩和した解決策が取られました。",
  "recommendedAction": "接続プールの上限を再度確認し、外部決済ゲートウェイに対する負荷を見直すことをお勧めします。",
  "assignedTeam": "Payment Team (連絡先: payment-team@example.com, Slack: #payment-support, オンコール: +81-3-0000-0001)"
}
```

## アプリケーション仕様

- `POST /tickets/analyze`
  - 問い合わせ本文だけをもとに、要約・カテゴリー・優先度・推奨アクション・担当チームを返します。
- `POST /tickets/investigate`
  - 問い合わせ内容に応じて LLM が Tool を選択し、調査結果を返します。
  - 利用可能な Tool:
    - `checkServiceStatus`: サービスの現在の稼働状況を確認
    - `searchSimilarIncidents`: 過去の類似インシデントを検索
    - `getSupportTeamContact`: 担当チームの連絡先を取得
- Tool が参照するデータは固定の JSON ファイルです。
  - `src/main/resources/data/service-status.json`
  - `src/main/resources/data/incidents.json`
  - `src/main/resources/data/support-teams.json`

## 主な依存関係

- `quarkus-rest-jackson`
- `quarkus-langchain4j-openai`
- `quarkus-arc`
