
(ns app.storage.frontend.media-viewer.subs
    (:require [mid-fruits.candy :refer [param return]]
              [re-frame.api     :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-directory-id
  [db [_ viewer-id]]
  (get-in db [:storage :media-viewer/meta-items :directory-id]))

(defn get-default-item-filename
  [db [_ viewer-id]]
  (let [directory-id (r get-directory-id db viewer-id)]
       (get-in db [:storage :media-viewer/data-items directory-id :media/default-item])))

(defn get-current-item-filename
  [db [_ viewer-id]]
  (if-let [item-filename (get-in db [:storage :media-viewer/meta-items :current-item])]
          (return item-filename)
          (r get-default-item-filename db viewer-id)))

(defn get-current-item-props
  [db [_ viewer-id]]
  {:item-filename (r get-current-item-filename db viewer-id)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :storage.media-viewer/get-current-item-props get-current-item-props)
