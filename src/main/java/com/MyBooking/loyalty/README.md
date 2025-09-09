### com.mybooking.loyalty — Programme de fidélité

- Rôle: gérer le compte de points des clients (solde, historique) et l’utilisation de points sur une réservation.

- Sous-dossiers attendus:
  - `controller`: `LoyaltyController` (balance, history, redeem).
  - `service`: `LoyaltyService` (earn/redeem, règles de plafond, atomicité, intégrité avec reservation.used_points).
  - `domain`: `LoyaltyAccount`, `LoyaltyTransaction`, enums `LoyaltyTxType`.
  - `repository`: `LoyaltyAccountRepository`, `LoyaltyTransactionRepository`.
  - `dto`: `BalanceDto`, `HistoryItemDto`, `RedeemRequest`, `RedeemResultDto`.

- Endpoints couverts:
  - `GET /api/v1/loyalty/balance`, `GET /api/v1/loyalty/history`, `POST /api/v1/loyalty/redeem`.

- Liens MCD/MPD/UML:
  - Tables: `loyalty_account` (1–1 avec user), `loyalty_transaction` (EARN/REDEEM, reservation_id nullable).
  - Classes: `LoyaltyAccount`, `LoyaltyTransaction`, `LoyaltyTxType`.

- Notes d’implémentation:
  - Transactions DB (SERIALIZABLE/optimistic) pour éviter le double spending.
  - Publishing d’événements de domaine optionnel (`PointsRedeemedEvent`).
