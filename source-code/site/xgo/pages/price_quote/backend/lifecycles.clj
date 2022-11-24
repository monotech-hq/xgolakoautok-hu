
(ns site.xgo.pages.price-quote.backend.lifecycles
    (:require [x.core.api :as x.core]))

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:x.router/add-route! :price-quote/route
                                  {:client-event   [:price-quote/load-page!]
                                   :js-build       :site
                                   :route-template "/:category/:model/:type/arajanlat"}]]}})
