
(ns app.clients.backend.viewer.helpers
    (:require [x.locales.api :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-item<-name-field
  [{:keys [request]} {:client/keys [first-name last-name] :as client-item}]
  ; XXX#7601
  ; Ha az item-viewer plugin kliens-oldali kezelője {:auto-title? true} beállítással van használva,
  ; akkor a :client/name virtuális mezőt szükséges hozzáadni a dokumentumhoz!
  (let [name-order (x.locales/request->name-order request)]
       (case name-order :reversed (assoc client-item :client/name (str last-name  " " first-name))
                                  (assoc client-item :client/name (str first-name " " last-name)))))
