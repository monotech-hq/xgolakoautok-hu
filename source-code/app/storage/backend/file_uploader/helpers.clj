
(ns app.storage.backend.file-uploader.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->files-data
  ; @param (map) request
  ;  {:params (map)
  ;    {:query (string)}}
  ;
  ; @example
  ;  (request->files-data {:params {"0" {:tempfile #object[java.io.File 0x4571e67a "/my-tempfile.tmp"}
  ;                                 "1" {:tempfile #object[java.io.File 0x4571e67a "/your-tempfile.tmp"}
  ;                                 :query [...]}})
  ;  =>
  ;  {"0" {:tempfile "/my-tempfile.tmp"}
  ;   "1" {:tempfile "/your-tempfile.tmp"}}
  ;
  ; @return (map)
  [{:keys [params]}]
  (letfn [(f [files-data dex file-data] (assoc files-data dex (update file-data :tempfile str)))]
         (reduce-kv f {} (dissoc params :query))))

(defn request->total-size
  [{:keys [params]}]
  (letfn [(f [total-size _ {:keys [size]}] (+ total-size size))]
         (reduce-kv f 0 (dissoc params :query))))
