
(ns app.contents.backend.handler.lifecycles
    (:require [app.contents.backend.handler.routes :as handler.routes]
              [x.core.api                          :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:x.router/add-route! :contents.handler/download-content
                                         {:route-template "/contents/:content-id"
                                          :get {:handler handler.routes/download-content}}]})
