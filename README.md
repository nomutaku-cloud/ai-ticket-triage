# ai-ticket-triage

Quarkus LangChain4j を使用したチケットトリアージのサンプルアプリケーションです。
問い合わせ内容（テキスト）を受け取り、LLM が要約・カテゴリー・優先度などを構造化データとして返します。

## 前提条件

- Java 21 以上
- OpenAI API キー

```shell
export OPENAI_API_KEY="your_api_key_here"
```

## 起動

```shell
./mvnw quarkus:dev
```

## 使用例

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

## 主な依存関係

- `quarkus-rest-jackson`
- `quarkus-langchain4j-openai`
