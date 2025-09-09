### com.mybooking.feedback — Avis & réponses

- Rôle: permettre au client de laisser un avis (rating 1–5 + commentaire) lié à une réservation, et à l’admin d’y répondre.

- Sous-dossiers attendus:
  - `controller`: `FeedbackController` (client), `AdminFeedbackController` (admin).
  - `service`: `FeedbackService`.
  - `domain`: `Feedback`, `FeedbackReply`.
  - `repository`: `FeedbackRepository`, `FeedbackReplyRepository`.
  - `dto`: `CreateFeedbackRequest`, `FeedbackDto`, `ReplyRequest`, `ReplyDto`.

- Endpoints couverts:
  - Client: `POST /api/v1/feedbacks`, `GET /api/v1/client/feedbacks`.
  - Admin: `GET /api/v1/admin/feedbacks`, `GET /api/v1/admin/feedbacks/{id}`, `POST /api/v1/admin/feedbacks/{id}/reply`.

- Liens MCD/MPD/UML:
  - Tables: `feedback`, `feedback_reply`.
  - Classes: `Feedback`, `FeedbackReply`.

- Notes d’implémentation:
  - Rating 1..5 (validation); un avis par séjour logique (à décider: contrainte unique reservation_id + user_id).
