
(ns app.clients.frontend.lister.subs
    (:require [mid-fruits.string :as string]
              [re-frame.api      :as r :refer [r]]
              [x.locales.api     :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  [db [_ item-dex]]
  (let [first-name  (get-in db [:clients :lister/downloaded-items item-dex :first-name])
        last-name   (get-in db [:clients :lister/downloaded-items item-dex :last-name])
        client-name (r x.locales/get-ordered-name db first-name last-name)]
      ; XXX#6071
      (string/trim client-name)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :clients.lister/get-client-name get-client-name)
