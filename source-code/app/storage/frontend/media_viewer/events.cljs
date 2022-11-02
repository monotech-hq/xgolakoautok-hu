
(ns app.storage.frontend.media-viewer.events
    (:require [re-frame.api :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn step-bwd!
  [db [_ viewer-id]])

(defn step-fwd!
  [db [_ viewer-id]])

(defn store-directory-item!
  [db [_ viewer-id {:media/keys [id] :as directory-item}]]
  (assoc-in db [:storage :media-viewer/data-items id] directory-item))

(defn receive-directory-item!
  [db [_ viewer-id server-response]]
  (let [directory-item (get server-response :storage.media-browser/get-item)]
       (r store-directory-item! db viewer-id directory-item)))

(defn load-viewer!
  [db [_ viewer-id viewer-props]]
  (assoc-in db [:storage :media-viewer/meta-items] viewer-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-db :storage.media-viewer/receive-directory-item! receive-directory-item!)
