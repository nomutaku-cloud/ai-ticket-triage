# ai-ticket-triage

Quarkus LangChain4j を使用したチケットトリアージのサンプルアプリケーションです。
問い合わせ内容（テキスト）を受け取り、LLM が MCP Server の Tool を呼び出しながら調査結果を構造化データとして返します。
チケットトリアージアプリ本体と、Tool を提供する MCP Server（`support-ops-mcp-server`）の 2 つのアプリケーションで構成されています。

## 前提条件

- Java 25
- OpenAI API キー

```shell
export OPENAI_API_KEY="your_api_key_here"
```

## 起動

MCP Server とトリアージアプリの 2 つを別々のターミナルで起動します。

### 1. MCP Server を起動（ポート 8081）

```shell
cd support-ops-mcp-server
./mvnw quarkus:dev
```

### 2. トリアージアプリを起動（ポート 8080）

```shell
./mvnw quarkus:dev
```

## 使用例

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
  "investigationResult": "payment-service の状態は DEGRADED で、外部決済ゲートウェイへのレイテンシが増加しています。過去の類似インシデントでは、接続タイムアウトが多発しており、接続プールの上限を拡張し、スロットリング設定を緩和した解決策が取られました。担当チームへの連絡先: payment-team@example.com、Slack: #payment-support",
  "recommendedAction": "接続プールの上限を再度確認し、外部決済ゲートウェイに対する負荷を見直すことをお勧めします。",
  "assignedTeam": "Payment Team"
}
```

## アプリケーション仕様

### トリアージアプリ（ポート 8080）

- `POST /tickets/investigate`
  - 問い合わせ内容に応じて LLM が MCP Tool を選択して調査し、結果を返します。

### MCP Server（ポート 8081）

- トランスポート: Streamable HTTP（`http://localhost:8081/mcp`）
- 提供する Tool:
  - `getServiceStatus`: 指定サービスの現在の稼働状況を確認する
  - `searchSimilarIncidents`: 過去の類似インシデントをキーワードで検索する（最大 3 件）
  - `getTeamContact`: 指定サービスの担当チーム連絡先（メール・Slack）を取得する
- Tool が参照するデータは固定の JSON ファイルです。
  - `support-ops-mcp-server/src/main/resources/data/service-status.json`
  - `support-ops-mcp-server/src/main/resources/data/incidents.json`
  - `support-ops-mcp-server/src/main/resources/data/support-teams.json`

## 主な依存関係

### トリアージアプリ

- `quarkus-rest-jackson`
- `quarkus-langchain4j-openai`
- `quarkus-langchain4j-mcp`
- `quarkus-arc`

### MCP Server

- `quarkus-rest-jackson`
- `quarkus-mcp-server-http`
- `quarkus-arc`
