
(ns app.storage.frontend.media-selector.helpers
    (:require [app.storage.frontend.core.config :as core.config]
              [io.api                           :as io]
              [re-frame.api                     :as r]
              [x.app-media.api                  :as x.media]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-id-f
  ; @param (string) media-link
  ;
  ; @example
  ;  (media-selector.helpers/import-id-f {:id "my-media" :uri "/.../my-media.txt"})
  ;  =>
  ;  "my-media"
  ;
  ; @return (string)
  [{:media/keys [id]}]
  ; A tárhely előre feltöltött mintafájlja kivételt képez, mivel a fájlnév
  ; nem a dokumentum azonosítójából származik.
  (case id "sample" core.config/SAMPLE-FILE-ID id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) media-id
  ; @param (map) media-item
  ;  {:filename (string)
  ;   :id (string)}
  ; @param (string) media-count
  ;
  ; @example
  ;  (media-selector.helpers/export-item-f "my-media" {...} 1)
  ;  =>
  ;  "/.../my-media.txt"
  ;
  ; @return (namespaced map)
  ;  {:media/id (string)
  ;   :media/uri (string)}
  [_ {:keys [filename id]} _]
  (if id {:media/id  id
          :media/uri (x.media/filename->media-storage-uri filename)}))
