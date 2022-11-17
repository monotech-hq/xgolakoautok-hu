
(ns app.storage.frontend.file-uploader.events
    (:require [dom.api      :as dom]
              [map.api      :refer [dissoc-in]]
              [re-frame.api :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-uploader-props!
  [db [_ uploader-id uploader-props]]
  (assoc-in db [:storage :file-uploader/meta-items uploader-id] uploader-props))

(defn init-uploader!
  [db [_ uploader-id]]
  (let [file-selector (dom/get-element-by-id "storage--file-selector")
        files-meta    (dom/file-selector->files-meta file-selector)
        files-data    (dom/file-selector->files-data file-selector)]
       (-> db (assoc-in  [:storage :file-uploader/selected-files uploader-id] files-data)
              (update-in [:storage :file-uploader/meta-items     uploader-id] merge files-meta))))

(defn clean-uploader!
  [db [_ uploader-id]]
  (-> db (dissoc-in [:storage :file-uploader/selected-files uploader-id])
         (dissoc-in [:storage :file-uploader/meta-items     uploader-id])))

(defn toggle-file-upload!
  [db [_ uploader-id file-dex]]
  (let [filesize (get-in db [:storage :file-uploader/selected-files uploader-id file-dex :filesize])]
       (if-let [file-cancelled? (get-in db [:storage :file-uploader/selected-files uploader-id file-dex :cancelled?])]
               (-> db (update-in [:storage :file-uploader/selected-files uploader-id file-dex :cancelled?] not)
                      (update-in [:storage :file-uploader/meta-items     uploader-id :files-size] + filesize)
                      (update-in [:storage :file-uploader/meta-items     uploader-id :file-count]   inc))
               (-> db (update-in [:storage :file-uploader/selected-files uploader-id file-dex :cancelled?] not)
                      (update-in [:storage :file-uploader/meta-items     uploader-id :files-size] - filesize)
                      (update-in [:storage :file-uploader/meta-items     uploader-id :file-count]   dec)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-db :storage.file-uploader/toggle-file-upload! toggle-file-upload!)
(r/reg-event-db :storage.file-uploader/clean-uploader!     clean-uploader!)
