(ns metabase.api.meta.field
  (:require [compojure.core :refer [GET PUT]]
            [metabase.api.common :refer :all]
            [metabase.db :refer :all]
            (metabase.models [hydrate :refer [hydrate]]
                             [field :refer [Field]])))

(defendpoint GET "/:id" [id]
  (->404 (sel :one Field :id id)
         read-check
         (hydrate [:table :db])))

(defendpoint PUT "/:id" [id :as {{:keys [special_type preview_display description]} :body}]
  (write-check Field id)
  (upd Field id :special_type special_type :preview_display preview_display :description description))

(defendpoint GET "/:id/summary" [id]
  (let-404 [{:keys [count distinct-count] :as field} (sel :one Field :id id)]
    (read-check field)
    [[:count @count]
     [:distincts @distinct-count]]))

;; ## TODO - Endpoints not yet implemented
;; (defendpoint GET "/:id/values" [id])
;; (defendpoint GET "/:id/foreignkeys" [id])

(define-routes)