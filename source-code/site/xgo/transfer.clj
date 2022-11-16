
(ns site.xgo.transfer
    (:require [io.api               :as io]
              [x.core.api    :as x.core]
              [mongo-db.api         :as mongo-db]
              [mid-fruits.normalize :as normalize]))

(defn convert [key-fn data]
  (letfn [(vec->map [m v] (assoc m (key-fn v) v))]
         (reduce vec->map {} data)))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn transfer-website-content-f
  [request]
  (io/read-edn-file "environment/website-content.edn"))

(x.core/reg-transfer! ::transfer-website-content!
  {:data-f      transfer-website-content-f
   :target-path [:site :website-content]})

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn transfer-website-config-f
  [request]
  (io/read-edn-file "environment/website-config.edn"))

(x.core/reg-transfer! ::transfer-website-config!
  {:data-f      transfer-website-config-f
   :target-path [:site :website-config]})


;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(def categories-projection
     #:category{:name        1
                :order       1
                :models      1
                :description 1})

(defn transfer-categories-f
  [request]
  (let [data (mongo-db/get-collection "categories" categories-projection)]
    (convert #(-> % :category/name (normalize/clean-text "-+")) data)))

(x.core/reg-transfer! ::transfer-categories!
  {:data-f      transfer-categories-f
   :target-path [:site :categories]})

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn transfer-models-f
  [request]
  (let [data (mongo-db/get-collection "models")]
    (convert #(-> % :model/id) data)))

(x.core/reg-transfer! ::transfer-models!
  {:data-f      transfer-models-f
   :target-path [:site :models]})

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn transfer-types-f
  [request]
  (let [data (mongo-db/get-collection "types")]
    (convert #(-> % :type/id) data)))

(x.core/reg-transfer! ::transfer-types!
  {:data-f      transfer-types-f
   :target-path [:site :types]})
