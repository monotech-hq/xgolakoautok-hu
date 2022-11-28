
(ns app.storage.backend.media-lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; XXX#4880
; A storage.media-lister engine a kliens-oldali media-picker elemben megjelenített
; media elemek listáját szolgálja ki.
(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :storage.media-lister
                                              {:collection-name "storage"
                                               :handler-key     :storage.media-lister
                                               :item-namespace  :media}]})
