
(ns app.storage.frontend.media-picker.sample
    (:require [app.storage.frontend.api :as storage]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-media-picker
  []
  [storage/media-picker ::my-media-picker
                        {:value-path [:my-item]}])
