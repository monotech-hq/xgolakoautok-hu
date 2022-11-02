
(ns app.storage.backend.file-uploader.prototypes
    (:require [app.storage.backend.core.helpers :as core.helpers]
              [io.api                           :as io]
              [mongo-db.api                     :as mongo-db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-item-prototype
  [{:keys [file-path filename size]}]
  ; - A fájlokkal ellentétben a mappák {:media/mime-type "..."} tulajdonsága nem állapítható meg a nevükből
  ; - A fájlok {:media/mime-type "..."} tulajdonsága is eltárolásra kerül, hogy a mappákhoz hasonlóan
  ;   a fájlok is rendelkezzenek {:media/mime-type "..."} tulajdonsággal
  (let [file-id            (mongo-db/generate-id)
        generated-filename (core.helpers/file-id->filename file-id filename)
        mime-type          (io/filename->mime-type filename)]
       {:media/alias       filename
        :media/description ""
        :media/filename    generated-filename
        :media/id          file-id
        :media/mime-type   mime-type
        :media/path        file-path
        :media/size        size}))
