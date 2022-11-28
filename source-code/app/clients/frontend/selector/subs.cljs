
(ns app.clients.frontend.selector.subs
    (:require [re-frame.api  :as r :refer [r]]
              [string.api    :as string]
              [x.locales.api :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  [db [_ item-dex]]
  (let [first-name  (get-in db [:clients :selector/downloaded-items item-dex :first-name])
        last-name   (get-in db [:clients :selector/downloaded-items item-dex :last-name])
        client-name (r x.locales/get-ordered-name db first-name last-name)]
      ; XXX#6071 (source-code/app/clients/frontend/viewer/subs.cljs)
      (string/trim client-name)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :clients.selector/get-client-name get-client-name)
