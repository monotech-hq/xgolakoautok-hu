
(ns app.storage.frontend.file-uploader.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-upload-files-query
  [db [_ uploader-id]]
  (let [destination-id (get-in db [:storage :file-uploader/meta-items uploader-id :destination-id])]
       [`(storage.file-uploader/upload-files! ~{:destination-id destination-id})]))
