
(ns app.storage.frontend.capacity-handler.subs
    (:require [engines.item-browser.api :as item-browser]
              [re-frame.api             :as r :refer [r]]))
 
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-max-upload-size
  ; @return (B)
  [db _]
  (or (get-in db [:storage :media-selector/browsed-item :max-upload-size])
      (get-in db [:storage :media-browser/browsed-item  :max-upload-size])))

(defn get-used-capacity
  ; @return (B)
  [db _]
  (or (get-in db [:storage :media-selector/browsed-item :used-capacity])
      (get-in db [:storage :media-browser/browsed-item  :used-capacity])))

(defn get-total-capacity
  ; @return (B)
  [db _]
  (or (get-in db [:storage :media-selector/browsed-item :total-capacity])
      (get-in db [:storage :media-browser/browsed-item  :total-capacity])))

(defn get-free-capacity
  ; @return (B)
  [db _]
  (let [used-capacity  (r get-used-capacity  db)
        total-capacity (r get-total-capacity db)]
       (- total-capacity used-capacity)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :storage.capacity-handler/get-max-upload-size get-max-upload-size)
(r/reg-sub :storage.capacity-handler/get-free-capacity   get-free-capacity)
