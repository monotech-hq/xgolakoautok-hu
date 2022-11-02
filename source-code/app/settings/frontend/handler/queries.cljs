
(ns app.settings.frontend.handler.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-update-user-settings-query
  ; @param (map) user-settings
  ;
  ; @return (vector)
  [db [_ user-settings]]
  [`(~(symbol :settings.handler/update-user-settings!)
     ~{:user-settings user-settings})])
