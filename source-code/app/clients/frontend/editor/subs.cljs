
(ns app.clients.frontend.editor.subs
    (:require [re-frame.api  :as r :refer [r]]
              [string.api    :as string]
              [x.locales.api :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-name
  [db _]
  (let [first-name  (get-in db [:clients :editor/edited-item :first-name])
        last-name   (get-in db [:clients :editor/edited-item :last-name])
        client-name (r x.locales/get-ordered-name db first-name last-name)]
       ; XXX#6071
       (string/trim client-name)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-sub :clients.editor/get-client-name get-client-name)
