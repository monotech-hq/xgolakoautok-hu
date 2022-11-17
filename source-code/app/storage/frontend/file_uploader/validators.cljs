
(ns app.storage.frontend.file-uploader.validators
    (:require [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upload-files-response-valid?
  [db [_ uploader-id server-response]]
  (let [file-count (get-in db [:storage :file-uploader/meta-items uploader-id :file-count])
        response   (get    server-response `storage.file-uploader/upload-files!)]
       (vector/count? response file-count)))
