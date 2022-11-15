
(ns app.storage.backend.core.helpers
    (:require [candy.api :refer [return]]
              [io.api    :as io]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-id->filename
  ; @param (string) file-id
  ; @param (string) filename
  ;
  ; @example
  ;  (core.helpers/file-id->filename "my-item" "my-image.png")
  ;  =>
  ;  "my-item.png"
  ;
  ; @return (string)
  [file-id filename]
  (if-let [extension (io/filename->extension filename)]
          (str    file-id "." extension)
          (return file-id)))
