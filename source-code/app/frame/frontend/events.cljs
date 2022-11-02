
(ns app.frame.frontend.events
    (:require [mid-fruits.vector :as vector]
              [re-frame.api      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-tool!
  ; @param (map) tool-props
  ;  {:group (metamorphic-content)
  ;   :label (metamorphic-content)
  ;   :route (string)}
  [db [_ tool-props]]
  (update-in db [:frame :tool-handler/data-items] vector/conj-item tool-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-db :frame/add-tool! add-tool!)
