
(ns app.storage.frontend.file-uploader.helpers
    (:require [io.api             :as io]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.string  :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  [uploader-id]
  ; A file-uploader által indított kéréseket egyedi azonosítóval szükséges ellátni,
  ; hogy egyszerre párhuzamosan több fájlfeltöltési folyamat is futtatható legyen!
  (keyword/add-namespace :storage.file-uploader uploader-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn uploader-props->allowed-extensions-list
  [{:keys [allowed-extensions]}]
  (let [allowed-extensions (or allowed-extensions (vals io/EXTENSIONS))]
       (str "." (string/join allowed-extensions ", ."))))
