
(ns app.storage.frontend.file-uploader.subs
    (:require [app.storage.frontend.capacity-handler.subs :as capacity-handler.subs]
              [app.storage.frontend.file-uploader.helpers :as file-uploader.helpers]
              [dom.api                                    :as dom]
              [mid-fruits.candy                           :refer [param return]]
              [mid-fruits.map                             :as map]
              [mid-fruits.vector                          :as vector]
              [re-frame.api                               :as r :refer [r]]
              [x.app-db.api                               :as x.db]
              [x.app-sync.api                             :as x.sync]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-file-prop
  [db [_ uploader-id file-dex prop-key]]
  (get-in db [:storage :file-uploader/selected-files uploader-id file-dex prop-key]))

(defn get-selected-file-count
  [db [_ uploader-id]]
  (r x.db/get-item-count db [:storage :file-uploader/selected-files uploader-id]))

(defn get-uploading-file-count
  [db [_ uploader-id]]
  (let [selected-files (get-in db [:storage :file-uploader/selected-files uploader-id])]
       (vector/filtered-count selected-files #(-> % :cancelled? not))))

(defn get-files-size
  [db [_ uploader-id]]
  (get-in db [:storage :file-uploader/meta-items uploader-id :files-size]))

(defn all-files-cancelled?
  [db [_ uploader-id]]
  (letfn [(f [{:keys [cancelled?]}] (not cancelled?))]
         (not (some f (get-in db [:storage :file-uploader/selected-files uploader-id])))))

(defn get-non-cancelled-files
  [db [_ uploader-id]]
  (letfn [(f [o file-dex {:keys [cancelled?]}]
             (if-not cancelled? (conj   o file-dex)
                                (return o)))]
         (reduce-kv f [] (get-in db [:storage :file-uploader/selected-files uploader-id]))))

(defn get-form-data
  [db [_ uploader-id]]
  (let [non-cancelled-files (r get-non-cancelled-files db uploader-id)
        file-selector       (dom/get-element-by-id "storage--file-selector")]
       (dom/file-selector->form-data file-selector non-cancelled-files)))

(defn capacity-limit-exceeded?
  [db [_ uploader-id]]
  (let [storage-free-capacity (r capacity-handler.subs/get-free-capacity db)
        upload-size           (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])]
       (>= upload-size storage-free-capacity)))

(defn max-upload-size-reached?
  [db [_ uploader-id]]
  (let [max-upload-size (r capacity-handler.subs/get-max-upload-size db)
        upload-size     (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])]
       (> upload-size max-upload-size)))

(defn file-upload-in-progress?
  [db _]
  (letfn [(f [[uploader-id _]]
             (let [request-id (file-uploader.helpers/request-id uploader-id)]
                  (r x.sync/request-active? db request-id)))]
         (some f (get-in db [:storage :file-uploader/meta-items]))))

(defn get-uploader-ids
  [db _]
  (let [meta-items (get-in db [:storage :file-uploader/meta-items])]
       (-> meta-items map/get-keys vector/reverse-items)))

(defn get-uploader-progress
  [db [_ uploader-id]]
  (let [request-id (file-uploader.helpers/request-id uploader-id)]
       (r x.sync/get-request-progress db request-id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :storage.file-uploader/get-file-prop            get-file-prop)
(r/reg-sub :storage.file-uploader/get-selected-file-count  get-selected-file-count)
(r/reg-sub :storage.file-uploader/get-uploading-file-count get-uploading-file-count)
(r/reg-sub :storage.file-uploader/get-files-size           get-files-size)
(r/reg-sub :storage.file-uploader/all-files-cancelled?     all-files-cancelled?)
(r/reg-sub :storage.file-uploader/capacity-limit-exceeded? capacity-limit-exceeded?)
(r/reg-sub :storage.file-uploader/max-upload-size-reached? max-upload-size-reached?)
(r/reg-sub :storage.file-uploader/get-uploader-ids         get-uploader-ids)
(r/reg-sub :storage.file-uploader/get-uploader-progress    get-uploader-progress)
