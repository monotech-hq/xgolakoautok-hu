
(ns app.products.backend.api
    (:require [app.products.backend.editor.lifecycles]
              [app.products.backend.editor.mutations]
              [app.products.backend.editor.resolvers]
              [app.products.backend.lister.lifecycles]
              [app.products.backend.lister.mutations]
              [app.products.backend.lister.resolvers]
              [app.products.backend.preview.lifecycles]
              [app.products.backend.preview.resolvers]
              [app.products.backend.selector.lifecycles]
              [app.products.backend.viewer.lifecycles]
              [app.products.backend.viewer.mutations]
              [app.products.backend.viewer.resolvers]
              [app.products.backend.handler.helpers :as handler.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.products.backend.handler.helpers
(def get-product-price  handler.helpers/get-product-price)
(def get-products-price handler.helpers/get-products-price)
